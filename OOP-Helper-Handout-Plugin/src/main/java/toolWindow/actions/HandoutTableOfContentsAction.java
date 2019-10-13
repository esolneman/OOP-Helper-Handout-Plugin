package toolWindow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.web.WebEngine;
import org.graalvm.compiler.graph.Node;
import org.jetbrains.annotations.NotNull;
import toolWindow.HandoutContentScreen;

import java.lang.reflect.Field;

public class HandoutTableOfContentsAction extends AnAction {

    private HandoutContentScreen handoutContentScreen;

    public HandoutTableOfContentsAction(String text) {
        super(text);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        System.out.println(anActionEvent);
        System.out.println(anActionEvent.getPresentation().getText());
        String heading = anActionEvent.getPresentation().getText();
        handoutContentScreen = new HandoutContentScreen();
        handoutContentScreen.goToLocation(heading);
        displayTableOfContents();
    }

    private void displayTableOfContents() {

    }
}
