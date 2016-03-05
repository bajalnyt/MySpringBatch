import org.hsqldb.TransactionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.jsr.launch.JsrJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.JobContext;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.transaction.PlatformTransactionManager;


import javax.inject.Inject;
import java.util.Properties;

/**
 * Created by 908752 on 3/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/batch.xml",
        "classpath:test-context.xml"})
public class MyUnitTest {


    JsrJobOperator jsrJobOperator;

    @Mock
    private JobExplorer jobExplorer;
    @Mock
    private JobRepository jobRepository;
    private JobParametersConverter parameterConverter;

    @Mock
    private PlatformTransactionManager transactionManager;

    //@Inject
    //JobContext jobContext;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        jsrJobOperator = new JsrJobOperator( jobExplorer,  jobRepository,  new JobParametersConvertorSupport(), transactionManager);

    }

    @Test
    public void testSetup(){
        jsrJobOperator.start("ACER", new Properties());
    }




}
