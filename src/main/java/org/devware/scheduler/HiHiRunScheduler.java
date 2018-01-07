package org.devware.scheduler;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by sogla on 07/01/2018.
 */
@Component
public class HiHiRunScheduler {
    @Autowired
    private JobLauncher jobLauncher;

    @Qualifier("scheduledJob2")
    @Autowired
    private Job job2;

    public HiHiRunScheduler(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public HiHiRunScheduler() {

    }


    @Scheduled(cron = "${hi.hi.cron.job.expression}")
    public void run() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()+9)
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(job2, jobParameters);

            System.out.println("Exit status Hi Hi: " + execution.getStatus());
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        }
    }
}
