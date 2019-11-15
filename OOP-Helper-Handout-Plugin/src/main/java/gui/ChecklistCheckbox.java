package gui;

import javax.swing.*;

public class ChecklistCheckbox extends JPanel{
    private JCheckBox checkBox;
    private JTextField textField;
    private JPanel checklistCheckbox;

    public ChecklistCheckbox (String text) {
        super();
        checklistCheckbox = new JPanel();
        checkBox = new JCheckBox();
        textField = new JTextField(text);
        createContent();
    }

    private void createContent() {
        checklistCheckbox.add(checkBox);
        checklistCheckbox.add(textField);
        this.add(checklistCheckbox);
    }

}
