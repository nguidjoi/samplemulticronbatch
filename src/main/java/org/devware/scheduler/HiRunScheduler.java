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
public class HiRunScheduler {

    @Autowired
    private JobLauncher jobLauncher;
    @Qualifier("scheduledJob1")
    @Autowired
    private Job job;

    public HiRunScheduler(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public HiRunScheduler() {}

    @Scheduled(cron = "${hi.cron.job.expression}")
    public void run() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()+7)
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParameters);

            System.out.println("Exit status Hi: " + execution.getStatus());
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
