package toolWindow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import toolWindow.HandoutContentScreen;

public class HandoutTableOfContentsAction extends AnAction {

    private HandoutContentScreen handoutContentScreen;
    public HandoutTableOfContentsAction(String text, HandoutContentScreen handoutContentScreen) {
        super(text);
        this.handoutContentScreen = handoutContentScreen;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        String heading = anActionEvent.getPresentation().getText();
        System.out.println(heading);
        this.handoutContentScreen.goToLocation(heading);

    }

}
