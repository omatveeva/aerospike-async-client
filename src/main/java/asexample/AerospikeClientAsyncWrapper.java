package asexample;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.async.AsyncClient;
import com.aerospike.client.listener.RecordListener;
import com.aerospike.client.policy.Policy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Created by olga on 03.03.17.
 */
public class AerospikeClientAsyncWrapper {
    private final AsyncClient client;
    private CompletableFuture<Record> result = new CompletableFuture<>();

    public AerospikeClientAsyncWrapper(AsyncClient client) {
        this.client = client;
    }

    public CompletionStage<Record> get(Policy policy, Key key) {
        client.get(policy, new ReadHandler(), key);
        return result;
    }

    private class ReadHandler implements RecordListener {

        @Override
        public void onSuccess(Key key, Record record) {
            result.complete(record);
        }

        @Override
        public void onFailure(AerospikeException e) {
            result.completeExceptionally(e);
        }
    }

}
