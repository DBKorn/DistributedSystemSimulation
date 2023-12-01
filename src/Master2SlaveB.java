import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Master communicating to Slave B to give it jobs
 */

public class Master2SlaveB extends Thread implements Slave_B_CommonData{
    public Master2SlaveB() {
    }

    @Override
    public void run() {
        //int slaveBPort = 6789;
        Socket slaveBSocket;
        try {
            slaveBSocket = new Socket("127.0.0.1", bPort);
            PrintWriter outToSlaveB = new PrintWriter(slaveBSocket.getOutputStream(), true);
            while (true) {
                System.out.println("M2Sb (maybe) beginning spin");
                while (!Master.slaveBHasNext()) {
                    try {
                        Thread.sleep(1500); //sleeping for some time to stop incessant polling
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //gets job from queue and gives it to the slave
                System.out.println("M2Sb Spin finished or avoided");
                String nextJob = Master.getNextJobSlaveB();
                System.out.println("M2Sb next job is " + nextJob);
                outToSlaveB.println(nextJob);
                System.out.println("M2Sb sent job " + nextJob + ", restarting...");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


