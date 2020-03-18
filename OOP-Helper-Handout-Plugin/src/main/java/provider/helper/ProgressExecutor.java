package provider.helper;

import com.intellij.openapi.progress.ProgressManager;
import provider.RepoLocalStorageDataProvider;

// https://github.com/JetBrains/intellij-community/blob/master/platform/core-api/src/com/intellij/openapi/progress/ProgressManager.java
public class ProgressExecutor {

    public void runSynchronousProcess(Runnable runnableTask) {
        //https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000718619--Problem-with-the-IntelliL-IDEA-progress-bar
        ProgressManager.getInstance().runProcessWithProgressSynchronously(runnableTask, "Handout Daten werden heruntergeladen", true, RepoLocalStorageDataProvider.getProject());
    }
}

