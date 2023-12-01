

public class Slave_D extends Slave implements Slave_D_CommonData{
    public static void main(String[] args) {
        Slave s = new Slave_D(dPort);
        s.run();
    }


    public Slave_D(int port){
        super(port);
    }
    /**
     * Takes in a job and completes it. We used for loops and sleep for one second in each iteration to give info
     * as to when a job will complete
     * @param current
     * @throws InterruptedException
     */
    @Override
    protected void doJob(JobType current) throws InterruptedException {

        if(current == JobType.JOB_A){
            for (int i = 4; i > 0; i--) {
                System.out.println("Slave D Working on " + current + " for " + i + " more seconds");
                Thread.sleep(1_000);
            }

        } else if (current == JobType.JOB_B){
            for (int i = 5; i > 0; i--) {
                System.out.println("Slave D Working on " + current + " for " + i + " more seconds");
                Thread.sleep(1_000);
            }
        } else if (current == JobType.JOB_C){
            for (int i = 6; i > 0; i--) {
                System.out.println("Slave D Working on " + current + " for " + i + " more seconds");
                Thread.sleep(1_000);
            }
        } else {
            for (int i = 7; i > 0; i--) {
                System.out.println("Slave D Working on " + current + " for " + i + " more seconds");
                Thread.sleep(1_000);
            }
        }
    }
}
