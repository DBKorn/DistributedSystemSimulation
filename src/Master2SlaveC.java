import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Master communicating to Slave C to give it jobs
 */

public class Master2SlaveC extends Thread implements Slave_C_CommonData{
    public Master2SlaveC() {
    }

    @Override
    public void run() {

        Socket slaveCSocket;
        try {
            slaveCSocket = new Socket("127.0.0.1", cPort);
            PrintWriter outToSlaveC = new PrintWriter(slaveCSocket.getOutputStream(), true);
            while (true) {
                System.out.println("M2Sb (maybe) beginning spin");
                while (!Master.slaveCHasNext()) {
                    try {
                        Thread.sleep(1500); //sleeping for some time to stop incessant polling
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //gets job from queue and gives it to the slave
                System.out.println("M2Sb Spin finished or avoided");
                String nextJob = Master.getNextJobSlaveC();
                System.out.println("M2Sb next job is " + nextJob);
                outToSlaveC.println(nextJob);
                System.out.println("M2Sb sent job " + nextJob + ", restarting...");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


