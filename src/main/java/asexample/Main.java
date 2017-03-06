package asexample;

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.async.AsyncClient;
import com.aerospike.client.policy.WritePolicy;

import java.util.concurrent.*;

/**
 * Created by olga on 03.03.17.
 */
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WritePolicy policy = new WritePolicy();
        policy.timeout = 50;  // 50 millisecond timeout.

        try (AsyncClient client = new AsyncClient("127.0.0.1", 3000)) {
            AerospikeClientAsyncWrapper wrapper = new AerospikeClientAsyncWrapper(client);

            Key key = new Key("test", "testset", "key1");
            wrapper.put(policy, key, new Bin("name", "hello"), new Bin("value", 12345), new Bin("abc", "def"));

            CompletionStage<Record> recordStage =  wrapper.get(policy, key);
            recordStage
                    .thenApply(record -> record.getString("abc"))
                    .thenAccept(System.out::println)
                    .toCompletableFuture().get();
        }
    }

}
