import io.infinicast.JArray;
import io.infinicast.JObject;
import io.infinicast.JToken;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by ASG on 28.04.2016.
 */
public class JsonTest {

    @Test
    public void TestTranspiledCode() {
        JObject json = JObject
                .Parse(" {\"requestId\":40,\"endpointObject\":{\"path\":\"\\/APLAY\\/endpoints\\/7Ex_1\\/\",\"endpoint\":\"7Ex_1\",\"data\":{}},\"errorCode\":0,\"type\":\"GetRoleForPathResult\",\"list\":[\"client.chat\",\"default\",\"chatter.moderator\",\"chatter.default\",\"chatter.voice\"]}");

        ArrayList<String> list = new ArrayList<String>();

        JArray arr = json.getJArray("list");
        for (JToken t : arr) {
            list.add(t.toString());
        }

        Assert.assertEquals("client.chat", list.get(0));
        Assert.assertEquals("default", list.get(1));
    }


    /**
     * Regression test for bug fix
     * https://app.asana.com/0/83023564931413/137463195531330
     */
    @Test
    public void preservesNullAttribute() {
        JObject json = new JObject();
        json.set("null_key", (String) null);
        assertEquals(json.get("null_key"), null);
    }
}
