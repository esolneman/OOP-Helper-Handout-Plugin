package gui;

import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ChecklistTable extends JTable{

    private static final long serialVersionUID = 1L;
    private JTable table;

    public ChecklistTable() {
        Object[] columnNames = {"Angabe", "Checked"};
        Object[][] data = {
                {"Buy", false},
                {"Sell",  true},
                {"Sell",  true},
                {"Buy",  false}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model) {

            private static final long serialVersionUID = 1L;
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    default:
                        return Boolean.class;
                }
            }
        };
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
    }
}