package toolWindow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import gui.EditChecklistDialog;
import org.jetbrains.annotations.NotNull;

public class EditChecklist extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        System.out.println("actionPerformed: Edit Checklist");
        EditChecklistDialog editChecklistDialog = new EditChecklistDialog();
        editChecklistDialog.showPanel();
    }

    private void createJOptionPane() {
    }
}
