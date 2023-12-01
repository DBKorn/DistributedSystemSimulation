import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * Super class for the slaves
 */

public abstract class Slave {
    protected final int port;
    protected BufferedReader inFromMaster;
    protected PrintWriter outToS2C;
    protected Socket listen2Master;
    protected Socket send2Master;
    JobType currentJob;
    String jobStr;

    public Slave(int port) {
        this.port = port;
    }


    public void run() {
        try {
            connect();
            while (true) {
                jobStr = inFromMaster.readLine(); //block until we have a job to do
                System.out.println("Slave line read: " + jobStr);
                currentJob = JobType.valueOf(jobStr.substring(jobStr.indexOf("||") + 2));
                doJob(currentJob);
                //Inform the master that we just finished a job
                outToS2C.println(jobStr);
                System.out.println("Slave finished! Restarting...");
            }
        } catch (SocketException se) {
            System.out.println("Master disconnected");
        } catch
        (IOException | InterruptedException e) {
            disconnect();
            e.printStackTrace();
        }
    }


    /**
     * This method connects to the master and sets up the input and output streams
     * that will be used to communicate with it.
     */
    protected void connect() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            listen2Master = serverSocket.accept();
            inFromMaster = new BufferedReader(new InputStreamReader(listen2Master.getInputStream()));
            send2Master = new Socket("127.0.0.1", 54321);
            outToS2C = new PrintWriter(send2Master.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void disconnect() {
        try {
            outToS2C.close();
            inFromMaster.close();
            listen2Master.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes in a job and completes it
     * @param current
     * @throws InterruptedException
     */
    protected abstract void doJob(JobType current) throws InterruptedException;

}
