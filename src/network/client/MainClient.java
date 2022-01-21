package network.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)){
            //reading the input from server
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));

            //returning the output to the server : true statement is to flush the buffer otherwise
            //we have to do it manuallyy
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            //taking the user input
            Scanner scanner = new Scanner(System.in);
            String userInput;
            String response;

            ClientRunnable clientRun = new ClientRunnable(socket);


            new Thread(clientRun).start();
            //loop closes when user enters exit command


            do {
                    String message = (" message : " );
                    System.out.println(message);
                    userInput = scanner.nextLine();
                    output.println(message + " " + userInput);
                    if (userInput.equals("exit")) {
                        //reading the input from server
                        break;
                    }
            } while (!userInput.equals("exit"));




        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e.getStackTrace());
        }
    }
}

