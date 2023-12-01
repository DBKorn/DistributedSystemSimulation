import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * (name "master" is just to contrast it to Slaves2ClientThread class, as opposed to it being part of the Master)
 * Waits for slaves to connect to master, does actual communication on Slaves2ClientThread thread
 */
public class Slaves2ClientMaster extends Thread {
    @Override
    public void run() {
        ServerSocket listenForSlaves = null;
        Socket slaveSocket = null;
        try {
            listenForSlaves = new ServerSocket(54321);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                System.out.println("Slaves2ClientMaster Started Again");
                slaveSocket = listenForSlaves.accept(); //waits for slave to connect to client
            } catch (IOException e) {
                System.out.println(e);
            }
            System.out.println("S2Cm starting S2Ct...");
            new Slaves2ClientThread(slaveSocket).start();
        }
    }


}
