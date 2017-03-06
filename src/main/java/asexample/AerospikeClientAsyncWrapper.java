package asexample;

import com.aerospike.client.*;
import com.aerospike.client.async.AsyncClient;
import com.aerospike.client.listener.RecordListener;
import com.aerospike.client.listener.WriteListener;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by olga on 03.03.17.
 */
public class AerospikeClientAsyncWrapper {
    private final AsyncClient client;

    public AerospikeClientAsyncWrapper(AsyncClient client) {
        this.client = client;
    }

    public CompletionStage<Record> get(Policy policy, Key key) {
        CompletableFuture<Record> result = new CompletableFuture<>();
        client.get(policy, new ReadHandler(result), key);
        return result;
    }

    private class ReadHandler implements RecordListener {
        private final CompletableFuture<Record> result;

        ReadHandler(CompletableFuture<Record> result) {
            this.result = result;
        }

        @Override
        public void onSuccess(Key key, Record record) {
            result.complete(record);
        }

        @Override
        public void onFailure(AerospikeException e) {
            result.completeExceptionally(e);
        }
    }

    public CompletableFuture<Void> put(WritePolicy policy, Key key, Bin... bins) {
        CompletableFuture<Void> result = new CompletableFuture<>();
        client.put(policy, new WriteHandler(result), key, bins);
        return result;
    }

    private class WriteHandler implements WriteListener {
        private final CompletableFuture<Void> result;

        private WriteHandler(CompletableFuture<Void> result) {
            this.result = result;
        }

        @Override
        public void onSuccess(Key key) {
            result.complete(null);
        }

        @Override
        public void onFailure(AerospikeException e) {
            result.completeExceptionally(e);
        }
    }
}
