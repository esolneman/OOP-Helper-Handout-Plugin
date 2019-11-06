package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import javafx.scene.web.WebView;
import objects.SpecificAssessmentCriteria;
import provider.LocalStorageDataProvider;
import webView.WebViewController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;

public class SpecificAssessmentCriteriaScreen {
    private SimpleToolWindowPanel toolWindowPanel;
    private SpecificAssessmentCriteria data;
    private JPanel assessmentCriteriaContent;
    private JTable criteriaTable;



    public SpecificAssessmentCriteriaScreen(ToolWindow toolWindow) {
        DefaultTableModel model = new DefaultTableModel();

        criteriaTable.setEnabled(false);
        criteriaTable.setFillsViewportHeight(true);
        data = LocalStorageDataProvider.getSpecificAssessmentCriteria();

        for (String s : data.getHeadline()) {
            model.addColumn(s);
        }
        /*for (String[][] criterion : data.getCriteria()) {
            for (String[] strings : criterion) {
                System.out.println("Data: " + Arrays.toString(strings));
            }
            model.addRow(criterion);
        }*/

        for (String[] criterion : data.getCriteria()) {
            System.out.println("ROW: " + Arrays.toString(criterion));
            model.addRow(criterion);
        }
        criteriaTable.setModel(model);


        //String[] columnNames = {"First Name", "Last Name"};
        //Object[][] data2 = {{"Kathy", "Smith"},{"John", "Doe"}};
        //criteriaTable = new JTable(data2, columnNames);


        //TableColumn tableColumn = new TableColumn(data.getHeadline().size());
        //criteriaTable.addColumn(new TableColumn(data.getHeadline().size()));
    }

    private void createTable() {

    }

    /*    public JPanel getContent() {
        return toolWindowPanel;
    }*/

    public JPanel getContent() {
        return assessmentCriteriaContent;
    }
}
