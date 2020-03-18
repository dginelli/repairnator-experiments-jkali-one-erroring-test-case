package uk.ac.ebi.eva.accession.pipeline.test;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.PlatformTransactionManager;

import uk.ac.ebi.eva.accession.pipeline.configuration.AccessionWriterConfiguration;
import uk.ac.ebi.eva.accession.pipeline.configuration.ChunkSizeCompletionPolicyConfiguration;
import uk.ac.ebi.eva.accession.pipeline.configuration.InvalidVariantSkipPolicyConfiguration;
import uk.ac.ebi.eva.accession.pipeline.configuration.VariantProcessorConfiguration;
import uk.ac.ebi.eva.accession.pipeline.configuration.VcfReaderConfiguration;
import uk.ac.ebi.eva.accession.pipeline.configuration.jobs.CreateSubsnpAccessionsJobConfiguration;
import uk.ac.ebi.eva.accession.pipeline.configuration.jobs.steps.CheckSubsnpAccessionsStepConfiguration;
import uk.ac.ebi.eva.accession.pipeline.configuration.jobs.steps.CreateSubsnpAccessionsStepConfiguration;

import javax.sql.DataSource;

@EnableAutoConfiguration
@Import({CreateSubsnpAccessionsJobConfiguration.class,
        CreateSubsnpAccessionsStepConfiguration.class, CheckSubsnpAccessionsStepConfiguration.class,
        VcfReaderConfiguration.class, VariantProcessorConfiguration.class, AccessionWriterConfiguration.class,
        ChunkSizeCompletionPolicyConfiguration.class, InvalidVariantSkipPolicyConfiguration.class})
public class BatchTestConfiguration {

    @Autowired
    private BatchProperties properties;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils();
    }

}