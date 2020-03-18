package handoutMenuActions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import provider.HandoutContentDataProvider;

//menu action for updating content
public class HandoutMenuUpdateContent extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        HandoutContentDataProvider.getInstance().updateHandoutData();
    }
}
