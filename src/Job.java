import java.io.*;
import java.net.*;

/**
 * Class that encapsulates a job with all extra info we need
 */

public class Job extends Thread {
    private String input;
    private Socket masterSocket;
    private int jobID = Client.getJobID();
    private String outStr = "";
    private JobType job;
    PrintWriter outToMaster;

    public Job(String input, Socket masterSocket) {
        this.input = input;
        this.masterSocket = masterSocket;
    }

    /**
     * prints info to console, assigns the enum JobType, sends to master to get job done
     */
    public void run() {
        try {
            System.out.println("Received " + input + " as input, your ID is " + jobID);
            job = getJobType(input);
            outStr += Client.getResponsePort() + "|" + jobID + "||" + job;
            outToMaster = new PrintWriter(masterSocket.getOutputStream(), true);
            outToMaster.println(outStr);
            System.out.println("Sent " + outStr + " to master, terminating thread...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JobType getJobType(String input) {
        if (input.equals("A")) {
            return JobType.JOB_A;
        } else if (input.equals("B")){
            return JobType.JOB_B;
        } else if (input.equals("C")){
            return JobType.JOB_C;
        } else {
            return JobType.JOB_D;
        }
    }
}