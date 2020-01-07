package provider.helper;

import com.intellij.openapi.progress.ProgressManager;
import provider.RepoLocalStorageDataProvider;

//https://howtodoinjava.com/java/multi-threading/executor-service-example/
public class ProgressExecutor {

    public void runSynchronousProcess(Runnable runnableTask) {
        //https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000718619--Problem-with-the-IntelliL-IDEA-progress-bar
        ProgressManager.getInstance().runProcessWithProgressSynchronously(runnableTask, "Handout Daten werden heruntergeladen", true, RepoLocalStorageDataProvider.getProject());
    }
}

