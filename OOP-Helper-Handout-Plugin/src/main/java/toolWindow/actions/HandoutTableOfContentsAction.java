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

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        HandoutContentScreen handoutContentScreen = new HandoutContentScreen();
        handoutContentScreen.goToLocation("#Bewertungskriterien");
        displayTableOfContents();
    }

    private void displayTableOfContents() {

    }
}
