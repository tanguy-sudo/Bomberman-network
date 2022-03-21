package network.server;

import models.BombermanGame;
import models.Game;
import models.strategy.BombermanManualStrategy;
import org.json.JSONObject;
import utils.AgentAction;
import utils.ColorAgent;
import utils.InfoAgent;
import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author tanguy, guillaume
 * Chaque client a un thread
 * ServerThreadListen gère la réception d'un objet JSON sous la forme d'une string
 * Gestion de la connexion avec le serveur web
 * Gestion de la demande de création d'une game via l'API du serveur web
 * Gestion de la demande de création d'une play via l'API du serveur web
 */
public class ServerThreadListen extends Thread {
    public static int compteur = 0;
    private Socket socket;
    private BombermanGame bombermanGame;
    private int playerNumber;
    private ArrayList<ServerThreadSend> threadList;
    private String username;
    private String password;
    private String urlServeurWeb;

    /**
     * Constructeur
     * @param socket
     * @param bombermanGame
     * @param threads
     */
    public ServerThreadListen(Socket socket, Game bombermanGame, ArrayList<ServerThreadSend> threads) {
        this.socket = socket;
        this.bombermanGame = (BombermanGame) bombermanGame;
        this.playerNumber = ServerThreadListen.compteur;
        this.bombermanGame.getpListBombermanAgent().get(this.playerNumber).setpStrategy(new BombermanManualStrategy());
        ServerThreadListen.compteur++;
        this.threadList = threads;
        this.username = "";
        this.password = "";

        Properties pros;
        pros = new Properties();
        FileInputStream ip = null;

        try {
            ip = new FileInputStream("./src/resources/config.properties");
            // Charge le fichier de configuration
            pros.load(ip);
            this.urlServeurWeb = pros.getProperty("webServerAddress");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * crée une entrée sur un socket, pour lire dessus
     * li l'action que le client envoie et modifie le jeu
     */
    @Override
    public synchronized void run() {
        try {
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            String outputString;
            AgentAction action;
            Boolean exit;
            while(true) {
                // li ce que le client a envoyé
                outputString = input.readLine();
                JSONObject j = new JSONObject(outputString);

                if(j != null){

                    // si le joueur a quitté la partie, on arrête et on ferme le socket
                    if(j.has("exit")){
                        exit = j.getBoolean("exit");
                        System.out.println("fin de partie");
                        socket.close();
                        break;
                    }
                    /*
                    * on regarde si l'utilisateur a envoyé les informations de connexion et s'il son différent de ce qu'il a
                    * envoyé précédemment puis on fait une demande de connexion auprès du serveur web
                    */
                    if(j.has("username") && j.has("password")){
                        String u = j.getString("username");
                        String p = j.getString("password");
                        if(!(this.username.equals(u) && this.password.equals(p))){
                            this.username = j.getString("username");
                            this.password = j.getString("password");
                            connectBDD();
                        }
                    }

                    action = (AgentAction) j.getEnum(AgentAction.class, "action");
                    // Mets à jour le jeu
                    this.bombermanGame.updateActionUser(action, this.playerNumber);

                    // Si c'est la fin de partie on fait demande de création de play au serveur web
                    if(!bombermanGame.gameContinue()){
                        createPlayBDD();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error occured " +e.getMessage());
            try {
                this.socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Vérifie si l'utilisateur exite en BDD
     */
    private void connectBDD(){
        try{
            HttpClient client = HttpClient.newHttpClient();

            JSONObject body = new JSONObject();
            body.put("username", this.username);
            body.put("password", this.password);

            // requête HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.urlServeurWeb + "user"))
                    .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(body)))
                    .header("Accept", "application.json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            Integer status = json.getInt("status");
            //Vérification de ce que retourne le serveur
            if (status == 201) {
                System.out.println("success");
                ColorAgent color = json.getEnum(ColorAgent.class, "couleur_agent");
                // Mets à jour la couleur du bomberman du joueur
                this.bombermanGame.getpListBombermanAgent().get(this.playerNumber).getpInfoAgent().setColor(color);

                // Créer le thread qui gère l'envoi d'informations
                ServerThreadSend serverThreadSend = new ServerThreadSend(socket, threadList, this.bombermanGame);
                serverThreadSend.start();
                // Si c'est le dernier joueur qui se connecte, on crée une game en BDD via l'API du serveur web
                if(this.playerNumber == this.bombermanGame.getpListBombermanAgent().size() - 1){
                    createGameBDD();
                    // Lance la partie
                    this.bombermanGame.launch();
                }
            } else if (status == 404) {
                System.out.println("error");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Créer une play en BDD via l'API du serveur web
     * une play correspond à une table qui associe un joueur et une game
     */
    private void createPlayBDD(){
        try {
            HttpClient client = HttpClient.newHttpClient();

            JSONObject body = new JSONObject();
            body.put("username", this.username);
            body.put("password", this.password);
            body.put("results", getResultsGame());
            body.put("id_game", this.threadList.get(0).getId_game());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.urlServeurWeb + "play"))
                    .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(body)))
                    .header("Accept", "application.json")
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        }  catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée une demande de création d'une GAME en BDD via l'API du serveur web
     */
    private void createGameBDD() {
        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.urlServeurWeb + "game"))
                    .POST(HttpRequest.BodyPublishers.ofString("WithoutParam"))
                    .header("Accept", "application.json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            int id_game = json.getInt("id_game");

            // Mets à jour l'attribut id_game pour chacun d'utilisateur
            for(ServerThreadSend threadSend : this.threadList) {
                threadSend.setId_game(id_game);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return une STRING qui indique si l'utilisateur à gagné
     */
    private String getResultsGame() {
        int countAgent = 0;
        int countEnemy = 0;

        for (InfoAgent agent : this.bombermanGame.fusionListAgent()) {
            if (agent.getType() == 'B') countAgent++;
            else countEnemy++;
        }

        String stringResults = "";
        if (countAgent == 0 && countEnemy == 0) {
            // égalité
            stringResults = "égalité";
        } else if (countAgent == 0) {
            // perdu
            stringResults = "perdu";
        } else if (countEnemy == 0) {
            // gagné
            stringResults = "gagné";
        }

        return stringResults;
    }
}
