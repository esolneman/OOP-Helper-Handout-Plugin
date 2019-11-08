package toolWindow.actionGroups;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import provider.HandoutContentHandler;
import toolWindow.actions.HandoutTableOfContentsAction;
import webView.WebViewController;

import java.util.ArrayList;

//https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000105804-Adding-PopupMenu-to-ActionBarButton
public class HandoutContentActionGroup extends ActionGroup {

    private static WebViewController webViewController;

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        ArrayList<String> headings = getHeadings();
        AnAction[] actions = new AnAction[headings.size()];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new HandoutTableOfContentsAction(headings.get(i), webViewController);
        }
        return actions;
    }

    public void setWebViewController(WebViewController webViewController){
        this.webViewController = webViewController;
    }

    private ArrayList<String> getHeadings() {
        return HandoutContentHandler.getNavHeadings();
    }

    @Override
    public boolean isPopup() {
        return true;
    }
}
