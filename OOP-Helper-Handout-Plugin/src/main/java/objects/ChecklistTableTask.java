package objects;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ChecklistTableTask {
    public  SimpleStringProperty taskDescription;
    public  SimpleBooleanProperty checked;

    public ChecklistTableTask(String taskDescription, boolean checked) {
        this.taskDescription = new SimpleStringProperty(taskDescription);
        this.checked = new SimpleBooleanProperty(checked);
    }

    public String getTaskDescription() {
        return taskDescription.get();
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription.set(taskDescription);
    }

    public boolean getChecked() {
        return checked.get();
    }

    public void setChecked(Boolean checked) {
        this.checked.set(checked);
    }

}
