package toolWindow.actionGroups;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import provider.HandoutContentHandler;
import toolWindow.HandoutContentScreen;
import toolWindow.actions.HandoutTableOfContentsAction;

import java.util.ArrayList;

//https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000105804-Adding-PopupMenu-to-ActionBarButton
//
public class HandoutContentActionGroup extends ActionGroup {

    private static HandoutContentScreen handoutContentScreen;


    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        ArrayList<String> headings = getHeadings();
        AnAction[] actions = new AnAction[headings.size()];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new HandoutTableOfContentsAction(headings.get(i), handoutContentScreen);
        }
        return actions;
    }

    public void setHandoutContentScreen(HandoutContentScreen handoutContentScreen){
        this.handoutContentScreen = handoutContentScreen;
    }

    private ArrayList<String> getHeadings() {
        return HandoutContentHandler.getNavHeadings();
    }

    @Override
    public boolean isPopup() {
        return true;
    }
}
