import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 * user uses this class to submit jobs to Distributed System
 */
public class Client {

    static Scanner scanner = new Scanner(System.in);
    static String input; //the job the user will enter
    static int jobID = 1; // the job ID. Is incremented for every job so each job has unique ID
    static Random r = new Random();
    static int responsePort = setResponsePort();

    /**
     * Generates a random 4 digit number to be the port number. We check that it's not in use with the while loop
     * @return the port number
     */
    private static int setResponsePort() {
        int testPortNumber = getRandom4DigitNum();
        while (!isPortAvailable(testPortNumber)) {
            testPortNumber = getRandom4DigitNum();
        }
        return testPortNumber;
    }

    /**
     *
     * @return a random number to be the port number
     */
    private static int getRandom4DigitNum() {
        return r.nextInt(8998) + 1001;
    }

    private static boolean isPortAvailable(int testPort) {
        try {
            ServerSocket testSocket = new ServerSocket(testPort);
            testSocket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * checks for job completed via ListenForJobCompletion
     * takes user input for a job and checks it for validation
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new ListenForJobCompletion().start();// How we get result of a job completed by the Master
        int masterPort = 12345; //arbitrarlity chosen,
        Socket masterSocket = new Socket("127.0.0.1", masterPort);
        while (true) {


            do{
                System.out.println("Enter \"A\" or \"B\" or \"C\" or \"D\"");
                input = scanner.nextLine().toUpperCase();
            } while (!kosherInput(input));


//            while (!(input.equals("A") || input.equals("B"))) {
//                System.out.println("Invalid job type, enter \"A\" or \"B\"");
//                input = scanner.nextLine().toUpperCase();
//            }
            new Job(input, masterSocket).start();
        }
    }
    public static boolean kosherInput(String s){
        return (s.length() == 1) && (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") );
    }

    public static synchronized int getJobID() {
        return jobID++;
    }

    public static int getResponsePort() {
        return responsePort;
    }

}
