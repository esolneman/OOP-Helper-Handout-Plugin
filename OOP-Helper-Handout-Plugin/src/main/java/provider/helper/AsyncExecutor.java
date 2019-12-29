package provider.helper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//https://howtodoinjava.com/java/multi-threading/executor-service-example/
public class AsyncExecutor {

    //TODO Executor Class with wait


    ExecutorService executor = Executors.newSingleThreadExecutor();

    public Future<String> runAsyncClone(Runnable runnableTask) {
        System.out.println("runAsyncClone");
        Future<String> result = executor.submit(runnableTask, "DONE");
        while (result.isDone() == false) {
            try {
                System.out.println("The method return value : " + result.get());
                break;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            //Sleep for 1 second
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Shut down the executor service
        System.out.println("Shutdown Executor");
        executor.shutdownNow();
        return result;
    }
}

