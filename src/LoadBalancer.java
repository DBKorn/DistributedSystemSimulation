import java.util.Arrays;

/**
 * We give jobs to SlaveA SlaveB or SlaveC depending on which Slave will finish all it's jobs
 * sooner. We use real time to do measure this.
 */
public class LoadBalancer extends Thread {
    private static long timeUntilSlaveAIsIdle;
    private static long timeUntilSlaveBIsIdle;
    private static long timeUntilSlaveCIsIdle;
    private static long timeUntilSlaveDIsIdle;

    public LoadBalancer() {}

    @Override
    public void run() {
        while (true) {
            System.out.println("LB (maybe) beginning spin");
            while (Master.unsortedJobs.size() == 0) {
                try {
                    Thread.sleep(1500); //wait for jobs to come in
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("LB Spin finished or avoided");
            String jobStr = Master.unsortedJobs.poll();
            JobType job = JobType.valueOf(jobStr.substring(jobStr.indexOf("||") + 2));

            //get what time (actual clock time) each slave would take to finish this individual job
            long[] times = new long[4];

            int i = 0;
            times[i++] = getTimeUntilSlaveAFinishesJob(job);
            times[i++] = getTimeUntilSlaveBFinishesJob(job);
            times[i++] = getTimeUntilSlaveCFinishesJob(job);
            times[i++] = getTimeUntilSlaveDFinishesJob(job);
            int minIndex = min(times);

            if (minIndex == 0){
                printSlaveWaitTimes(times);
                System.out.println("Adding \"" + jobStr + "\" to slave A");
                Master.slaveAJobs.add(jobStr);
                timeUntilSlaveAIsIdle = times[minIndex];
            } else if(minIndex == 1){
                printSlaveWaitTimes(times);
                System.out.println("Adding \"" + jobStr + "\" to slave B");
                Master.slaveBJobs.add(jobStr);
                timeUntilSlaveBIsIdle = times[minIndex];
            } else if (minIndex == 2){
                printSlaveWaitTimes(times);
                System.out.println("Adding \"" + jobStr + "\" to slave C");
                Master.slaveCJobs.add(jobStr);
                timeUntilSlaveCIsIdle = times[minIndex];
            } else if (minIndex == 3){
                printSlaveWaitTimes(times);
                System.out.println("Adding \"" + jobStr + "\" to slave D");
                Master.slaveDJobs.add(jobStr);
                timeUntilSlaveDIsIdle = times[minIndex];
            }

            System.out.println("LB Finished! Restarting...");
        }
    }
    private static void printSlaveWaitTimes(long[] a){
        int i = 0;
        System.out.println("timeUntilSlaveAFinishesJob: " + a[i++] + ", timeUntilSlaveBFinishesJob: " + a[i++] +
                ", timeUntilSlaveCFinishesJob: " + a[i++] + ", timeUntilSlaveDFinishesJob: " + a[i++] );

    }

    private static synchronized int min(long[] a){
        int mIndex = 0;
        for (int i = 1; i < a.length; i++) {
            if (a[i] < a[mIndex]){
                mIndex = i;
            }
        }
        return mIndex;
    }

    public static synchronized void balanceLoadIntake(String jobStr) {
        Master.unsortedJobs.add(jobStr);
    }

    /**
     * This comment is for both getTimeUntilSlaveAFinishesJob and getTimeUntilSlaveBFinishesJob methods.
     *
     * We first check how long it'll take a slave to finish the given job (jobTime variable)
     * Then we check when/if the slave will/is finish all jobs
     *
     * If it's idle, then if given this job, it'll finish in the time it'll take from now to finish this job
     * if it'll finish in the future, it'll finish in the time it will take to finish all it's other jobs + this new job
     *
     * Now, we can even take into account the a partially completed job
     *
     * Example: if the slave is idle, and System.currentTimeMillis() = 230pm, and jobTime is 2 minutes, return 232pm
     * if the slave is not idle, and it'll finish all jobs at 255pm and the new job will take 2 minutes, return 257pm
     * (I know System.currentTimeMillis() return time in milis after jan 1 1970, but that's not really the point here)
     * @param job
     * @return
     */

    private static long getTimeUntilSlaveAFinishesJob(JobType job) {
        long jobTime;
        if (job == JobType.JOB_A) {
            jobTime = 2000;
        }
        else {
            jobTime = 10_000;
        }
        if (System.currentTimeMillis() > timeUntilSlaveAIsIdle) { //the time it started idling is in the past (it already started idling)
            return System.currentTimeMillis() + jobTime; //the time it is now + time to complete this job, as it will finish this job
        }
        else {
            //it'll finish job in future
            return timeUntilSlaveAIsIdle + jobTime; //increment the time with the time it'll take to finish the new job
        }
    }

    private static long getTimeUntilSlaveBFinishesJob(JobType job) {
        long jobTime;
        if (job == JobType.JOB_B) {
            jobTime = 2000;
        }
        else {
            jobTime = 10_000;
        }
        if (System.currentTimeMillis() > timeUntilSlaveBIsIdle) {
            return System.currentTimeMillis() + jobTime;
        }
        else {
            return timeUntilSlaveBIsIdle + jobTime;
        }
    }

    private static long getTimeUntilSlaveCFinishesJob(JobType job) {
        long jobTime;
        if (job == JobType.JOB_C) {
            jobTime = 2000;
        }
        else {
            jobTime = 10_000;
        }
        if (System.currentTimeMillis() > timeUntilSlaveCIsIdle) {
            return System.currentTimeMillis() + jobTime;
        }
        else {
            return timeUntilSlaveCIsIdle + jobTime;
        }
    }

    private static long getTimeUntilSlaveDFinishesJob(JobType job) {
        long jobTime;

        if(job == JobType.JOB_A){
            jobTime = 4000;
        } else if (job == JobType.JOB_B){
            jobTime = 5000;
        } else if (job == JobType.JOB_C){
            jobTime = 6000;
        } else {
            jobTime = 7000;
        }

        if (System.currentTimeMillis() > timeUntilSlaveDIsIdle) {
            return System.currentTimeMillis() + jobTime;
        }
        else {
            return timeUntilSlaveDIsIdle + jobTime;
        }
    }
}
