package test;

import client.proxy.FileProxy;
import client.proxy.HttpProxy;
import client.proxy.UtilProxy;
import junit.framework.TestCase;
import org.junit.Test;
import shared.request.game.ChangeLogLevelRequest;

public class UtilProxyTest extends TestCase {

    @Test
    /**
     * Tests the utility proxy with a file proxy
     */
    public void testFileChangeLogLevel() {
        ChangeLogLevelRequest request = new ChangeLogLevelRequest("ALL");
        FileProxy proxy = new FileProxy("testJson/");
        UtilProxy util = new UtilProxy(proxy);
        assert (util.changeLogLevel(request) == 1);
    }

    @Test
    /**
     * Uses the server to run the tests
     */
    public void testHttpChangeLogLevel() {
        ChangeLogLevelRequest request = new ChangeLogLevelRequest("ALL");
        HttpProxy proxy = new HttpProxy("localhost", "8081");
        UtilProxy util = new UtilProxy(proxy);
        assert (util.changeLogLevel(request) == 1);
    }
}