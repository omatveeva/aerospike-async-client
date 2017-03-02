package asexample;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Created by olga on 03.03.17.
 */
public class AerospikeClientAsyncWrapper {
    private final AerospikeClient client;
    private final Executor executor;

    public AerospikeClientAsyncWrapper(AerospikeClient client, Executor executor) {
        this.client = client;
        this.executor = executor;
    }

    public CompletionStage<Record> get(Policy policy, Key key) {
        CompletableFuture<Record> result = new CompletableFuture<>();
        executor.execute(() -> {
            result.complete(client.get(policy, key));
        });
        return result;
    }


}
