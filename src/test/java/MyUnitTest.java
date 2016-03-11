import org.springframework.batch.core.*;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.hsqldb.TransactionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.jsr.launch.JsrJobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.JobContext;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.transaction.PlatformTransactionManager;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/batch.xml",
        "classpath:test-context.xml"})
public class MyUnitTest {


    @Autowired
    private Job job_i;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobLauncher jobLauncher_i;


    @Test
    public void testSetup() throws Exception {

        JobExecution jobExecution =  jobLauncherTestUtils.launchJob();

        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

    }

    @Test
    public void runJob() throws Exception {

        JobExecution jobExecution = jobLauncher_i.run(job_i, new JobParametersBuilder().toJobParameters());
        System.out.println(jobExecution.getExitStatus());
    }

}
