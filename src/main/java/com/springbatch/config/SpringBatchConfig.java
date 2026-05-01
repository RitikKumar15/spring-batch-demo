package com.springbatch.config;

import com.springbatch.entity.CoffeeEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class SpringBatchConfig {

    @Bean
    public ItemReader<CoffeeEntity> flatFileItemReader(@Value("${file.input}") Resource fileInput) {
        FlatFileItemReader<CoffeeEntity> itemReader = new FlatFileItemReader<>();
        itemReader.setName("coffeeItemReader");
        itemReader.setResource(fileInput);
        itemReader.setLinesToSkip(1);
        itemReader.setStrict(false);
        itemReader.setLineMapper(getLineMapper());
        return itemReader;
    }

    @Bean
    public LineMapper<CoffeeEntity> getLineMapper() {
        DefaultLineMapper<CoffeeEntity> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames("brand", "origin", "characteristics");
        lineMapper.setLineTokenizer(tokenizer);

        BeanWrapperFieldSetMapper<CoffeeEntity> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setStrict(false);
        fieldSetMapper.setTargetType(CoffeeEntity.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public ItemProcessor<CoffeeEntity, CoffeeEntity> itemProcessor() {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemWriter<CoffeeEntity> itemWriter(DataSource dataSource) {
        JdbcBatchItemWriter<CoffeeEntity> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setJdbcTemplate(new NamedParameterJdbcTemplate(dataSource));
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new);
        jdbcBatchItemWriter.setSql("INSERT INTO coffee_info (coffee_Id, brand, origin, characteristics) VALUES (:coffeeId, :brand, :origin, :characteristics)");
        return jdbcBatchItemWriter;
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
        ItemReader<CoffeeEntity> flatFileItemReader, ItemWriter<CoffeeEntity> itemWriter) {
        return new StepBuilder("step1", jobRepository)
                .<CoffeeEntity, CoffeeEntity>chunk(10, platformTransactionManager)
                .reader(flatFileItemReader)
                .processor(itemProcessor())
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step1)
//                .next(step1)
                .end()
                .build();
    }

}
