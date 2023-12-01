import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class checks that a job is completed and tells the client.
 * Takes info from master and prints to console for client
 */

public class ListenForJobCompletion extends Thread {
    ServerSocket listenForCompletion;
    private final int responsePort = Client.getResponsePort();

    public ListenForJobCompletion() {
    }


    @Override
    public void run() {
        try {
            listenForCompletion = new ServerSocket(responsePort);
            while (true) {
                System.out.println("LFC Made it here, waiting to accept...");// just to tell us where we are
                Socket listenSocket = listenForCompletion.accept();
                System.out.println("Accepted!");
                BufferedReader in = new BufferedReader(new InputStreamReader(listenSocket.getInputStream()));
                String masterResponse = in.readLine();// masterResponse is a string of info from master about completed job
                System.out.println("masterResponse: " + masterResponse);
                System.out.println("Received confirmation that Job " + masterResponse.substring(masterResponse.indexOf("||") + 2) +
                        " with ID number " + masterResponse.substring(masterResponse.indexOf("|") + 1, masterResponse.indexOf("||")) + " has finished");
                System.out.println("LFC Restarting...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
