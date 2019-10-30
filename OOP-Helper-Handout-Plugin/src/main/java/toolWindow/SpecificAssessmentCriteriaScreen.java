package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.table.JBTable;
import javafx.scene.web.WebView;
import objects.SpecificAssessmentCriteria;
import provider.LocalStorageDataProvider;
import webView.WebViewController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView;
import java.util.ArrayList;

public class SpecificAssessmentCriteriaScreen {
    //private final HandoutPluginFXPanel specificAssessmentCriteriaContent;
    private static WebView webView;
    private WebViewController webViewController;
    private SimpleToolWindowPanel toolWindowPanel;
    private SpecificAssessmentCriteria data;
    private JPanel assessmentCriteriaContent;
    private JTable criteriaTable;



    public SpecificAssessmentCriteriaScreen(ToolWindow toolWindow) {
        DefaultTableModel model = new DefaultTableModel();
        criteriaTable.setAutoCreateRowSorter(true);
        criteriaTable.setFillsViewportHeight(true);
        data = LocalStorageDataProvider.getSpecificAssessmentCriteria();
        for (String s : data.getHeadline()) {
            model.addColumn(s);
        }
        for (String[][] criterion : data.getCriteria()) {
            for (String[] strings : criterion) {
                
            }
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
