import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testSetup() throws Exception {

        JobExecution jobExecution =  jobLauncherTestUtils.launchJob();

        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        int count = jdbcTemplate.queryForList("select * from account").size();
        Assert.assertEquals(1, count);

    }

    // Another way to run the job
    @Test
    public void runJob() throws Exception {

        JobExecution jobExecution = jobLauncher_i.run(job_i, new JobParametersBuilder().toJobParameters());
        System.out.println(jobExecution.getExitStatus());
    }

}
