package toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import objects.SpecificAssessmentCriteria;
import provider.LocalStorageDataProvider;

import javax.swing.*;
import java.util.spi.LocaleNameProvider;

public class SpecificAssessmentCriteriaScreen {
    private JPanel specificAssessmentCriteriaContent;
    private JTable assessmentCriteriaTable;
    private SpecificAssessmentCriteria specificAssessmentCriteria;

    public SpecificAssessmentCriteriaScreen(ToolWindow toolWindow) {
        specificAssessmentCriteria = LocalStorageDataProvider.getSpecificAssessmentCriteria();
        createTable();
    }

    private void createTable() {

    }

    public JPanel getContent() {
        return specificAssessmentCriteriaContent;
    }
}
