package toolWindow.actions;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.profile.codeInspection.ui.inspectionsTree.InspectionConfigTreeNode;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.web.WebEngine;
import listener.OnEventListener;
import listener.OnInternLinkClickedListener;
import org.graalvm.compiler.graph.Node;
import org.jetbrains.annotations.NotNull;
import toolWindow.HandoutContentScreen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HandoutTableOfContentsAction extends AnAction {

    private HandoutContentScreen handoutContentScreen;
    private List<OnInternLinkClickedListener> listeners = new ArrayList<>();

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

    public void addListener(OnInternLinkClickedListener listener) {
        listeners.add(listener);
    }

}
