package toolWindow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindowManager;
import gui.EditChecklistDialog;
import org.jetbrains.annotations.NotNull;
import provider.RepoLocalStorageDataProvider;

public class EditChecklist extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        System.out.println("actionPerformed: Edit Checklist");
        //TODO Not Show Panel -> listener for Controller
        //EditChecklistDialog editChecklistDialog = new EditChecklistDialog();
       // System.out.println("TOOL ID WINDOW: " + ToolWindowManager.getInstance(RepoLocalStorageDataProvider.getProject()).getActiveToolWindowId());

        //ChecklistScreen checklistScreen = (ChecklistScreen) ToolWindowManager.getInstance(RepoLocalStorageDataProvider.getProject()).getToolWindow(ToolWindowManager.getInstance(RepoLocalStorageDataProvider.getProject()).getActiveToolWindowId());
        //checklistScreen.editUserChecklist();
        //editChecklistDialog.showPanel();
    }
}
