public class Slave_B extends Slave implements Slave_B_CommonData {

    public static void main(String[] args) {
        Slave s = new Slave_B(bPort);
        s.run();
    }


    public Slave_B(int port){
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
        if(current == JobType.JOB_B){
            for (int i = 2; i > 0; i--) {
                System.out.println("Slave B Working on " + current + " for " + i + " more seconds");
                Thread.sleep(1_000);
            }

        }
        else{
            for (int i = 10; i > 0; i--) {
                System.out.println("Slave B Working on " + current + " for " + i + " more seconds");
                Thread.sleep(1_000);
            }
        }
    }
}

