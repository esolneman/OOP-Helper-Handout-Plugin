package toolWindow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import webView.WebViewController;

public class HandoutTableOfContentsAction extends AnAction {

    private WebViewController webViewController;
    public HandoutTableOfContentsAction(String text, WebViewController webViewController) {
        super(text);
        this.webViewController = webViewController;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        String heading = anActionEvent.getPresentation().getText();
        this.webViewController.goToHeading(heading);
    }

}
