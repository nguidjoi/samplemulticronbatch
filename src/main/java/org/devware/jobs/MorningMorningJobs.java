package org.devware.jobs;

import org.devware.scheduler.HiHiRunScheduler;
import org.devware.tasklets.HiHiTasklet;
import org.devware.tasklets.HiTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/**
 * Created by Alain NGUIDJOI BELL on 07/01/2018.
 */
@Configuration
@EnableBatchProcessing
public class MorningMorningJobs {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public MorningMorningJobs(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job scheduledJob2() {
        return jobBuilderFactory.get("scheduledJob2").incrementer(new RunIdIncrementer()).start(step2()).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Hi Hi Hi!");
                    return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public HiHiRunScheduler hiHiRunScheduler() {
        HiHiRunScheduler hiHiRunScheduler = new HiHiRunScheduler();
        return hiHiRunScheduler;
    }
}
