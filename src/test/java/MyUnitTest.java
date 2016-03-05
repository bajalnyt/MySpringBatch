import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
/**
 * Created by 908752 on 3/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:test-context.xml"})
public class MyUnitTest {

    @Test
    public void testHello() {
        String message = "Hello World!";
        Assert.assertEquals(12, message.length());
    }

}
