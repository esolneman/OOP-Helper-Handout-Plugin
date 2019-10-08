package provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//https://howtodoinjava.com/java/multi-threading/executor-service-example/
public class AsyncExecutor {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    public Future<String> runAsyncClone(Runnable runnableTask){
        System.out.println("runAsyncClone");
        Future<String> result = executor.submit(runnableTask, "DONE");
        return result;
    }
}
