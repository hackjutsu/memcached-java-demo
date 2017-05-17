import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MemcachedJava {
    public static void main(String[] args) {

        try{
//            memcached_set();
//            memcached_add();
            memcached_test();

        }catch(Exception ex){
            System.out.println( ex.getMessage() );
        }
    }

    private static void memcached_set() throws IOException, ExecutionException, InterruptedException {

        MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        System.out.println("Connection to server sucessful.");

        // now set data into memcached server
        Future fo1 = mcc.set("key_set_1", 900, "value_set_1");
        System.out.println("set status:" + fo1.get());

        Future fo2 = mcc.set("key_set_2", 900, "value_set_2");
        System.out.println("set status:" + fo2.get());

        Future fo3 = mcc.set("key_set_3", 900, "value_set_3");
        System.out.println("set status:" + fo3.get());

        Future fo4 = mcc.set("key_set_4", 900, "value_set_4");
        System.out.println("set status:" + fo4.get());

        Future fo5 = mcc.set("key_set_5", 900, "value_set_5");
        System.out.println("set status:" + fo5.get());

        // retrieve and check the value from cache
        System.out.println("key_set_1 value in cache - " + mcc.get("key_set_1"));
        System.out.println("key_set_2 value in cache - " + mcc.get("key_set_2"));
        System.out.println("key_set_3 value in cache - " + mcc.get("key_set_3"));
        System.out.println("key_set_4 value in cache - " + mcc.get("key_set_4"));
        System.out.println("key_set_5 value in cache - " + mcc.get("key_set_5"));

        // Shutdowns the memcached client
        mcc.shutdown();
    }

    private static void memcached_add() throws IOException, ExecutionException, InterruptedException {

        // Connecting to Memcached server on localhost
        MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        System.out.println("Connection to server sucessful.");

        // add data to memcached server
        Future fo = mcc.set("key_add_1", 900, "value_add_1");

        // print status of set method
        System.out.println("set status:" + fo.get());

        // retrieve and check the value from cache
        System.out.println("key_add_1 value in cache - " + mcc.get("key_add_1"));

        // try to add data with existing key
        fo = mcc.add("key_add_1", 900, "value_add_1_another");

        // print status of set method
        System.out.println("add status:" + fo.get());

        // adding a new key to memcached server
        fo = mcc.add("key_add_2", 900, "value_add_2");

        // print status of set method
        System.out.println("add status:" + fo.get());

        // retrieve and check the value from cache
        System.out.println("key_add_2 value in cache - " + mcc.get("key_add_2"));

        // Shutdowns the memcached client
        mcc.shutdown();

    }

    private static void memcached_test() throws IOException {
        // Connecting to Memcached server on localhost
        MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        System.out.println("Connection to server sucessful.");

        // retrieve and check the value from cache
        System.out.println("key_python_1 value in cache - " + mcc.get("key_python_1"));

        // Shutdowns the memcached client
        mcc.shutdown();
    }
}