package services;

import java.util.concurrent.*;

public class AsyncExecutor {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public Future<String> runAsyncClone(Runnable runnableTask){
        System.out.println("runAsyncClone");
        Future<String> result = executor.submit(runnableTask, "DONE");
        return result;
    }


}
