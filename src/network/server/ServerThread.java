package network.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import controller.ControllerBombermanGame;
import models.BombermanGame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ServerThread extends Thread {
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void run() {
        try {
            //Reading the input from Client
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            
            //returning the output to the client : true statement is to flush the buffer otherwise
            //we have to do it manually
            output = new PrintWriter(socket.getOutputStream(),true);

            ControllerBombermanGame controllerBombermanGame = new ControllerBombermanGame();
            controllerBombermanGame.initGame("/home/etud/Documents/s8/reseau/Bomberman-network/layouts/niveau3.lay", "1", true);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            String objectString = objectMapper.writeValueAsString(controllerBombermanGame.getpGame());

            output.println(objectString);

            //inifite loop for server
            while(true) {
                String outputString = input.readLine();
                //if user types exit command
                if(outputString.equals("exit")) {
                    break;
                }
                printToALlClients(outputString);
                //output.println("Server says " + outputString);
                System.out.println("Server received " + outputString);

            }


        } catch (Exception e) {
            System.out.println("Error occured " +e.getMessage());
        }
    }

    private void printToALlClients(String outputString) {
        for( ServerThread sT: threadList) {
            sT.output.println(outputString);
        }
    }
}
