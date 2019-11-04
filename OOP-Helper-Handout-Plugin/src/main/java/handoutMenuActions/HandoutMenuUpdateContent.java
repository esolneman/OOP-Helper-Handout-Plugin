package handoutMenuActions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import provider.HandoutContentDataProvider;
import provider.HandoutContentDataProviderInterface;

public class HandoutMenuUpdateContent extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        //TODO AddListener to update content Data
        HandoutContentDataProvider.getInstance().updateHandoutData();
    }
}
