package gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import provider.RepoLocalStorageDataProvider;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InformCommitChangesDialog extends DialogWrapper {
    private String titleString;
    private String subtitleString;
    private ArrayList<String> commitMessagesList;
    private Integer numCommits;

    public  InformCommitChangesDialog(ArrayList<String> commitMessages) {
        super(RepoLocalStorageDataProvider.getProject(),true); // use current window as parent
        init();
        commitMessagesList = commitMessages;
        numCommits = commitMessagesList.size();
        titleString = "Eine Aktuelle Version der Content Daten wurde heruntergeladen";
        subtitleString = numCommits + "Änderungen wurden hinzugefügt:";
        setTitle(titleString);
        setOKActionEnabled(true);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("testing");
        label.setPreferredSize(new Dimension(100, 100));
        dialogPanel.add(label, BorderLayout.CENTER);
        return dialogPanel;
    }
}
