import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * tells client that a job was completed
 */
public class Slaves2ClientThread extends Thread {
    private final Socket listenToSlave;

    public Slaves2ClientThread(Socket listenToSlave) {
        this.listenToSlave = listenToSlave;
    }

    @Override
    public void run() {
        String inStr;
        try {
            while (true) {
                System.out.println("S2Ct made it here");
                //gets input from slave
                BufferedReader in = new BufferedReader(new InputStreamReader(listenToSlave.getInputStream()));
                inStr = in.readLine();
                System.out.println("S2Ct Line read: " + inStr);
                if (inStr == null) {
                    System.out.println("S2Ct line read was null, terminating thread...");
                    return;
                } else {
                    //send to client
                    int responsePort = Integer.parseInt(inStr.substring(0, inStr.indexOf("|")));
                    System.out.println("Client responsePort: " + responsePort);
                    Socket responseSocket = new Socket("127.0.0.1", responsePort);
                    PrintWriter outToClient = new PrintWriter(responseSocket.getOutputStream(), true);
                    outToClient.println(inStr);
                    outToClient.close();
                    responseSocket.close();
                    System.out.println("S2Ct Finished! Restarting...");
                }
            }
        } catch (SocketException se) {
            System.out.println("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
