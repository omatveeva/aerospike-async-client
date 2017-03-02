package asexample;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;

import java.util.concurrent.*;

/**
 * Created by olga on 03.03.17.
 */
public class Main {
    static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WritePolicy policy = new WritePolicy();
        policy.timeout = 50;  // 50 millisecond timeout.

        try (AerospikeClient client = new AerospikeClient("127.0.0.1", 3000)) {
            AerospikeClientAsyncWrapper wrapper = new AerospikeClientAsyncWrapper(client, executor);

            Key key = new Key("test", "testset", "key1");
            client.put(policy, key, new Bin("name", "hello"), new Bin("value", 12345));

            CompletionStage<Record> recordStage =  wrapper.get(policy, key);
            recordStage
                    .thenApply(record -> record.getInt("value"))
                    .thenAccept(System.out::println)
                    .toCompletableFuture().get();
        }

        executor.shutdown();
    }

}
