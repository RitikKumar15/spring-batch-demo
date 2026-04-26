package com.springbatch.controller;

import com.springbatch.entity.CoffeeEntity;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/importData")
    public List<CoffeeEntity> importData() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
            JobParametersInvalidException, JobRestartException {

        // run job
        JobExecution jobExecution = jobLauncher.run(job, new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters());

        // if job has completed successfully
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("!!! JOB FINISHED! Time to verify the results !!!");

            return new ArrayList<>(jdbcTemplate.query("SELECT * FROM coffee_info",
                    (rs, row) -> CoffeeEntity.builder()
                            .coffeeId(rs.getString(1))
                            .brand(rs.getString(2))
                            .origin(rs.getString(3))
                            .characteristics(rs.getString(4))
                            .build()));

        } else {
            System.err.println("!!! JOB FAILED! Batch Job Failed Please Check !!!");
            return Collections.emptyList();
        }
    }
}
