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
        return new AnAction[]{
                new HandoutTableOfContentsAction("Wichtige Informationen"),
                new HandoutTableOfContentsAction("#Bewertungskriterien")
        };
    }

    @Override
    public boolean isPopup() {
        return true;
    }
}
