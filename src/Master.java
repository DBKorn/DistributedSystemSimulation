import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Takes in the Job from client and uses LoadBalancer to assign jobs to appropriate Slave
 * it then gives them to the slaves. Here, it only connects to client. It does the
 * other stuff on the various threads it creates
 */

public class Master {
    static final Queue<String> unsortedJobs = new LinkedList<>();//for the jobs that haven't been load balanced yet
    static final Queue<String> slaveAJobs = new LinkedList<>();
    static final Queue<String> slaveBJobs = new LinkedList<>();
    static final Queue<String> slaveCJobs = new LinkedList<>();
    static final Queue<String> slaveDJobs = new LinkedList<>();



    public static void main(String[] args) {

        new LoadBalancer().start();
        //takes from the appropriate queue and gives to appropriate slave
        new Master2SlaveA().start();
        new Master2SlaveB().start();

        new Master2SlaveC().start();
        new Master2SlaveD().start();

        //test
        //new Master2AnySlave(/*hardcode int or implement interface?, make other interface methods of the stuff below */).start();

        new Slaves2ClientMaster().start();//to tell user job is finished
        Socket clientSocket = null;
        ServerSocket listenForClient = null;
        try {
            listenForClient = new ServerSocket(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                System.out.println("Master Started Again");
                clientSocket = listenForClient.accept();
            } catch (IOException e) {
                System.out.println(e);
            }
            new MasterThread(clientSocket).start();
        }
    }

    //looks redundant (could've put "slaveAJob.poll()" in Master2SlaveA), but if we took these out, it gave continuous output and didn't work
    public static String getNextJobSlaveA() {
        return slaveAJobs.poll();
    }

    public static String getNextJobSlaveB() {
        return slaveBJobs.poll();
    }

    public static String getNextJobSlaveC() {
        return slaveCJobs.poll();
    }

    public static String getNextJobSlaveD() {
        return slaveDJobs.poll();
    }


    public static boolean slaveAHasNext() {
        return !slaveAJobs.isEmpty();
    }

    public static boolean slaveBHasNext() {
        return !slaveBJobs.isEmpty();
    }

    public static boolean slaveCHasNext() {
        return !slaveCJobs.isEmpty();
    }

    public static boolean slaveDHasNext() {
        return !slaveDJobs.isEmpty();
    }
}

