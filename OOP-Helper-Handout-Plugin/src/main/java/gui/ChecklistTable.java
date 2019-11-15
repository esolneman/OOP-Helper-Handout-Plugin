package gui;

import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ChecklistTable extends JPanel{

    private static final long serialVersionUID = 1L;
    private JTable table;

    public ChecklistTable(DefaultTableModel model) {
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
        JScrollBar scrollBar = new JScrollBar();
        scrollBar.add(table);
        this.add(scrollBar);
    }

    public void setModel(DefaultTableModel model) {
        table.setModel(model);
    }
}