package org.devware.jobs;

import org.devware.scheduler.HiHiRunScheduler;
import org.devware.scheduler.HiRunScheduler;
import org.devware.tasklets.HiTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/**
 * Created by sogla on 07/01/2018.
 */

@Configuration
@EnableBatchProcessing
public class MorningJobs {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public MorningJobs(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job scheduledJob1() {
        return jobBuilderFactory.get("scheduledJob1").start(step1()).build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").tasklet((contribution, chunkContext) -> {
            System.out.println("Hi !");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public HiRunScheduler hiRunScheduler() {
        HiRunScheduler hiRunScheduler = new HiRunScheduler();
        return hiRunScheduler;
    }

}
