package objects;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ChecklistTableTask {
    public SimpleStringProperty taskDescription;
    public SimpleBooleanProperty checked;
    public SimpleStringProperty id;

    //TODO Quelle: Efefective Java Page 13-14 Kapitel 2.2 - Thema 2

    private ChecklistTableTask(ChecklistTableTask.TasksTableBuilder builder){
        taskDescription = builder.taskDescription;
        checked = builder.checked;
        id = builder.id;
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

    public static class TasksTableBuilder {

        private SimpleStringProperty taskDescription;
        private SimpleBooleanProperty checked;
        private SimpleStringProperty id;

        public TasksTableBuilder(String taskDescription, Boolean checked){
            this.taskDescription = new SimpleStringProperty(taskDescription);
            this.checked = new SimpleBooleanProperty(checked);
        }

        public ChecklistTableTask.TasksTableBuilder id(String id){
            this.id = new SimpleStringProperty(id);
            return this;
        }

        public ChecklistTableTask build(){
            return new ChecklistTableTask(this);
        }
    }

}
