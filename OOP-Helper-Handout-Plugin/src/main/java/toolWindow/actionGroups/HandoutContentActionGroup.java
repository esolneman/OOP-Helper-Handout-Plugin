package toolWindow.actionGroups;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HandoutContentActionGroup extends ActionGroup {
    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        return new AnAction[]{new MyAction("All"), new MyAction("Current Class")};
    }

    class MyAction extends AnAction {
        public MyAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
            System.out.println("HandoutContentActionGroup");
        }
    }

    @Override
    public boolean isPopup() {
        return true;
    }
}
