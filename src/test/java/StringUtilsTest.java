import io.activator.infinicast.client.utils.PathUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ASG on 21.06.2016.
 */
public class StringUtilsTest {

    @Test
    public void testStringUtils() {
        assertEquals("defg", PathUtils.getPathAddressElement("/abc/defg/dfgdfg/dfg/", 1));

        assertEquals("",PathUtils.getPathAddressElement("/",0));
        assertEquals("",PathUtils.getPathAddressElement(null,0));
        assertEquals("",PathUtils.getPathAddressElement("/dfgdfg/",1));
    }
}
