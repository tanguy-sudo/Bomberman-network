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
 * ServerThread gère l'envoie d'un objet JSON sous la forme d'une string a son client;
 */
public class ServerThreadListen extends Thread {
    public static int compteur = 0;
    private Socket socket;
    private BombermanGame bombermanGame;
    private int playerNumber;
    private ArrayList<ServerThreadSend> threadList;
    private String username;
    private String password;
    private static int id_game;
    private String urlServeurWeb;

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
            pros.load(ip);
            this.urlServeurWeb = pros.getProperty("webServerAddress");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * crée une entrée et une sortie sur un socket, pour lire et écrire dessus
     * li l'action que le client envoie et lui renvoie le jeu modifié
     */
    @Override
    public synchronized void run() {
        try {
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            String outputString;
            AgentAction action;
            Boolean exit;
            while(true) {

                outputString = input.readLine();
                JSONObject j = new JSONObject(outputString);

                if(j != null){

                    if(j.has("exit")){
                        exit = j.getBoolean("exit");
                        System.out.println("fin de partie");
                        break;
                    }
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
                    this.bombermanGame.updateActionUser(action, this.playerNumber);

                    if(!bombermanGame.gameContinue()){
                        createPlayBDD();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error occured " +e.getMessage());
        }
    }

    private void connectBDD(){
        try{
            HttpClient client = HttpClient.newHttpClient();

            JSONObject body = new JSONObject();
            body.put("username", this.username);
            body.put("password", this.password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.urlServeurWeb + "/BombermanWeb/api/user"))
                    .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(body)))
                    .header("Accept", "application.json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            Integer status = json.getInt("status");
            if (status == 201) {
                System.out.println("success");
                ColorAgent color = json.getEnum(ColorAgent.class, "couleur_agent");
                this.bombermanGame.getpListBombermanAgent().get(this.playerNumber).getpInfoAgent().setColor(color);

                ServerThreadSend serverThreadSend = new ServerThreadSend(socket, threadList, this.bombermanGame);
                serverThreadSend.start();
                if(this.playerNumber == this.bombermanGame.getpListBombermanAgent().size() - 1){
                    createGameBDD();
                    this.bombermanGame.launch();
                }
            } else if (status == 404) {
                System.out.println("error");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createPlayBDD(){
        try {
            HttpClient client = HttpClient.newHttpClient();

            JSONObject body = new JSONObject();
            body.put("username", this.username);
            body.put("password", this.password);
            body.put("results", getResultsGame());
            body.put("id_game", ServerThreadListen.id_game);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.urlServeurWeb + "/BombermanWeb/api/play"))
                    .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(body)))
                    .header("Accept", "application.json")
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        }  catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createGameBDD() {
        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.urlServeurWeb + "/BombermanWeb/api/game"))
                    .POST(HttpRequest.BodyPublishers.ofString("WithoutParam"))
                    .header("Accept", "application.json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            int id_game = json.getInt("id_game");
            ServerThreadListen.id_game = id_game;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

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
