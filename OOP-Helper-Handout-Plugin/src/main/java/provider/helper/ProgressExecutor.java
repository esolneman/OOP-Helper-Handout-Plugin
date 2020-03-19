package provider.helper;

import com.intellij.openapi.progress.ProgressManager;
import provider.RepoLocalStorageDataProvider;

//execute runnable synchronously with displaying a progress
public class ProgressExecutor {

    private static String PROGRESS_MESSAGE = "Handout Daten werden heruntergeladen";

    public void runSynchronousProcess(Runnable runnableTask) {
        //https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000718619--Problem-with-the-IntelliL-IDEA-progress-bar
        ProgressManager.getInstance().runProcessWithProgressSynchronously(runnableTask, PROGRESS_MESSAGE, true, RepoLocalStorageDataProvider.getProject());
    }
}

