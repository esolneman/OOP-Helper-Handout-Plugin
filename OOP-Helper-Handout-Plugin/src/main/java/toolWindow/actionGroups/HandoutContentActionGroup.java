package toolWindow.actionGroups;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import toolWindow.actions.HandoutTableOfContentsAction;

public class HandoutContentActionGroup extends ActionGroup {
    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        String[] headings = getHeadings();
        AnAction[] actions = new AnAction[headings.length];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new HandoutTableOfContentsAction(headings[i]);
            System.out.println(actions[i].getTemplatePresentation().getText());
        }
        return actions;
    }

    private String[] getHeadings() {
        //TODO getHeadings from html file with nav element -> name ?
        return new String[]{"Wichtige Informationen", "Bewertungskriterien"};
    }

    @Override
    public boolean isPopup() {
        return true;
    }
}
