package provider.helper;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;
import provider.RepoLocalStorageDataProvider;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//https://howtodoinjava.com/java/multi-threading/executor-service-example/
public class AsyncExecutor {

    //TODO Executor Class with wait


    ExecutorService executor = Executors.newSingleThreadExecutor();

    public void runAsyncClone(Runnable runnableTask) {
        System.out.println("runAsyncClone");
        //https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000718619--Problem-with-the-IntelliL-IDEA-progress-bar
        ProgressManager.getInstance().runProcessWithProgressSynchronously(runnableTask, "Handout Daten werden heruntergeladen", true, RepoLocalStorageDataProvider.getProject());


        /* Future<String> result = executor.submit(runnableTask, "DONE OHOH");
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
        return result;*/
    }
}

