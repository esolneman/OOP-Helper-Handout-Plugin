package toolWindow.actionGroups;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import provider.contentHandler.ContentHandlerInterface;
import provider.contentHandler.HandoutContentHandler;
import toolWindow.actions.HandoutTableOfContentsAction;
import webView.WebViewController;

import java.util.ArrayList;

//https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000105804-Adding-PopupMenu-to-ActionBarButton
public class HandoutContentActionGroup extends ActionGroup {

    private static WebViewController webViewController;
    private ArrayList<String> headings = new ArrayList<>();

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        System.out.println(" AnActionEvent HEADINGS: " + headings);

        AnAction[] actions = new AnAction[headings.size()];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new HandoutTableOfContentsAction(headings.get(i), webViewController);
        }
        return actions;
    }

    public void setWebViewController(WebViewController webViewController){
        this.webViewController = webViewController;
    }

    public void setHeadings(ArrayList<String> headings) {
        this.headings = headings;
        System.out.println("HEADINGS: " + headings);
    }



    @Override
    public boolean isPopup() {
        return true;
    }
}
