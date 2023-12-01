import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Master communicating to Slave D to give it jobs
 */

public class Master2SlaveD extends Thread implements Slave_D_CommonData{
    public Master2SlaveD() {
    }

    @Override
    public void run() {

        Socket slaveDSocket;
        try {
            slaveDSocket = new Socket("127.0.0.1", dPort);
            PrintWriter outToSlaveD = new PrintWriter(slaveDSocket.getOutputStream(), true);
            while (true) {
                System.out.println("M2Sb (maybe) beginning spin");
                while (!Master.slaveDHasNext()) {
                    try {
                        Thread.sleep(1500); //sleeping for some time to stop incessant polling
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //gets job from queue and gives it to the slave
                System.out.println("M2Sb Spin finished or avoided");
                String nextJob = Master.getNextJobSlaveD();
                System.out.println("M2Sb next job is " + nextJob);
                outToSlaveD.println(nextJob);
                System.out.println("M2Sb sent job " + nextJob + ", restarting...");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
