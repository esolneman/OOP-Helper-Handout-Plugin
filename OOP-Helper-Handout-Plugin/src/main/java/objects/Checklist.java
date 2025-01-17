package objects;

import java.util.ArrayList;

//concept of class hierarchy from Bloch, J. (2018). Effective Java: Best Practices für die Java-Plattform (3. Auflage Aufl.;
//D. Louis, Übers.). Heidelberg: dpunkt.verlag.

//object classes for predefined and own task lists
public class Checklist {

    public ArrayList<Task> tasks;

    public Task getTaskWithId(String currentRepoTaskID) {
        for (Task task : tasks) {
            if (task.id.equals(currentRepoTaskID)) {
                return task;
            }
        }
        return null;
    }

    public static class Task {
        public String taskDescription;
        public boolean checked;
        public String id;

        public void setDescription(String currentRepoTaskDescription) {
            taskDescription = currentRepoTaskDescription;
        }

        public static class TasksBuilder {

            private String taskDescription;
            private boolean checked;

            private String id;

            public TasksBuilder(String taskDescription, boolean checked){
                this.taskDescription = taskDescription;
                this.checked = checked;
            }

            public TasksBuilder id(String id){
                this.id = id;
                return this;
            }

            public Task build(){
                return new Task(this);
            }
        }

        private Task(TasksBuilder builder){
            taskDescription = builder.taskDescription;
            checked = builder.checked;
            id = builder.id;
        }
    }

    public ArrayList<Task> getTasks(){
        return tasks;
    }

    public void setTasks(ArrayList<Task>  tasks){
        this.tasks = tasks;
    }
}
