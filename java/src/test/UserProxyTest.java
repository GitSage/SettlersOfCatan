package test;

import client.proxy.FileProxy;
import client.proxy.UserProxy;
import junit.framework.TestCase;
import org.junit.Test;
import shared.request.game.UserRequest;


public class UserProxyTest extends TestCase {

    @Test
    /**
     * Obviously tests login
     */
    public void testLogin() throws Exception {
        //Create a fileproxy
        FileProxy proxy = new FileProxy("testJson/");
        UserProxy tester = new UserProxy(proxy);
        //Doesn't actually matter.  FileProxy will always return Success
        UserRequest request = new UserRequest("test", "test");
        //Basically did we successfully read the JSON file?
        assertTrue(tester.register(request) == 1);
    }

    @Test
    /**
     * Again this tests register
     */
    public void testRegister() throws Exception {
        //Create a fileproxy
        FileProxy proxy = new FileProxy("testJson/");
        UserProxy tester = new UserProxy(proxy);
        //Doesn't actually matter.  FileProxy will always return Success
        UserRequest request = new UserRequest("test", "test");
        //Basically did we successfully read the JSON file?
        assertTrue(tester.login(request) == 1);
    }
}