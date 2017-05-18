import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import net.spy.memcached.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Log4j
public class MemcachedJava {

    // https://projectlombok.org/features/Log.html
//    static Logger log = Logger.getLogger(MemcachedJava.class);

    public static void main(String[] args) {

        try{
//            memcached_set_primitive();
//            memcached_add();
//            memcached_test();
            memcached_set_object();
//            memcached_get_object();
//            memcached_pool();

        }catch(Exception ex){
            log.debug( ex.getMessage() );
        }
    }

    private static void memcached_set_primitive() throws IOException, ExecutionException, InterruptedException {

        MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        log.debug("Connection to server sucessful.");

        // now set data into memcached server
        Future fo1 = mcc.set("key_set_1", 900, "value_set_1");
        log.debug("set status:" + fo1.get());

        Future fo2 = mcc.set("key_set_2", 900, "value_set_2");
        log.debug("set status:" + fo2.get());

        Future fo3 = mcc.set("key_set_3", 900, "value_set_3");
        log.debug("set status:" + fo3.get());

        Future fo4 = mcc.set("key_set_4", 900, "value_set_4");
        log.debug("set status:" + fo4.get());

        Future fo5 = mcc.set("key_set_5", 900, "value_set_5");
        log.debug("set status:" + fo5.get());

        // retrieve and check the value from cache
        log.debug("key_set_1 value in cache - " + mcc.get("key_set_1"));
        log.debug("key_set_2 value in cache - " + mcc.get("key_set_2"));
        log.debug("key_set_3 value in cache - " + mcc.get("key_set_3"));
        log.debug("key_set_4 value in cache - " + mcc.get("key_set_4"));
        log.debug("key_set_5 value in cache - " + mcc.get("key_set_5"));

        // Shutdowns the memcached client
        mcc.shutdown();
    }

    private static void memcached_set_object() throws IOException {
        MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        log.debug("Connection to server sucessful.");

        ObjectMapper mapper = new ObjectMapper();
        Staff staff = new Staff("Ken", 20);

        //Object to JSON in String
        String staffInJson = mapper.writeValueAsString(staff);
        log.debug(staffInJson);

        // set object into memcached server
        mcc.set("key_set_java_object", 900 , staffInJson);
        System.out.print("key_set_java_object in cache - " + mcc.get("key_set_java_object"));

        // Shutdowns the memcached client
        mcc.shutdown();
    }

    private static void memcached_get_object() throws IOException {
        MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        log.debug("Connection to server sucessful.");

        ObjectMapper mapper = new ObjectMapper();
        String key = "key_set_python_object";
        String jsonInString = (String) mcc.get(key);

        //JSON to object
        Staff staff = mapper.readValue(jsonInString, Staff.class);
        log.debug(String.format("Staff's name is %s, age is %d.", staff.getName(), staff.getAge()));

        // Shutdowns the memcached client
        mcc.shutdown();
    }

    private static void memcached_add() throws IOException, ExecutionException, InterruptedException {

        // Connecting to Memcached server on localhost
        MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        log.debug("Connection to server sucessful.");

        // add data to memcached server
        Future fo = mcc.set("key_add_1", 900, "value_add_1");

        // print status of set method
        log.debug("set status:" + fo.get());

        // retrieve and check the value from cache
        log.debug("key_add_1 value in cache - " + mcc.get("key_add_1"));

        // try to add data with existing key
        fo = mcc.add("key_add_1", 900, "value_add_1_another");

        // print status of set method
        log.debug("add status:" + fo.get());

        // adding a new key to memcached server
        fo = mcc.add("key_add_2", 900, "value_add_2");

        // print status of set method
        log.debug("add status:" + fo.get());

        // retrieve and check the value from cache
        log.debug("key_add_2 value in cache - " + mcc.get("key_add_2"));

        // Shutdowns the memcached client
        mcc.shutdown();

    }

    private static void memcached_test() throws IOException {
        // Connecting to Memcached server on localhost
        MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        log.debug("Connection to server sucessful.");

        // retrieve and check the value from cache
        log.debug("key_python_1 value in cache - " + mcc.get("key_python_1"));

        // Shutdowns the memcached client
        mcc.shutdown();
    }

    private static void memcached_pool() throws IOException {
        // http://stackoverflow.com/a/10711496/3697757
        // http://dustin.sallings.org/java-memcached-client/apidocs/net/spy/memcached/MemcachedClient.html
        MemcachedClient mcc = new MemcachedClient(
                new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Cancel).build(),
                AddrUtil.getAddresses("127.0.0.1:11211 127.0.0.1:11212"));

        // Try to get a value, for up to 5 seconds, and cancel if it
        // doesn't return
        String key = "someKey1";
        String value = null;

        mcc.set(key, 900, "some_value");

        Future<Object> f = mcc.asyncGet(key);
        try {
            log.debug("Trying to get the value of " + key);
            value = (String)f.get(5, TimeUnit.SECONDS);
            log.debug(String.format("The value of %s is %s", key, value));
        } catch (Exception e) {
          // Since we don't need this, go ahead and cancel the operation.
          // This is not strictly necessary, but it'll save some work on
          // the server.  It is okay to cancel it if running.
          log.warn(String.format("Failed to get %s from memcached. Canceled.", key));
          f.cancel(true);
      }
    }
}