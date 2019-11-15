package objects;

import java.util.ArrayList;

public class Checklist {

    public ArrayList<Tasks> tasks;

    public static class Tasks {
        public String taskDescription;
        public boolean checked;
        public String id;

        //TODO Quelle: Efefective Java Page 13-14 Kapitel 2.2 - Thema 2
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

            public Tasks build(){
                return new Tasks(this);
            }
        }

        private Tasks (TasksBuilder builder){
            taskDescription = builder.taskDescription;
            checked = builder.checked;
            id = builder.id;
        }
    }

    public ArrayList<Tasks> getTasks(){
        return tasks;
    }

    public void setTasks(ArrayList<Tasks>  tasks){
        this.tasks = tasks;
    }
}
