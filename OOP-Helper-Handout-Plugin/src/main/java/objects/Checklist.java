package objects;

import java.util.ArrayList;

public class Checklist {

    private ArrayList<Tasks> tasks;

    public static class Tasks {
        private String task;
        private boolean checked;
        private Integer id;

        //TODO Quelle: Efefective Java Page 13-14 Kapitel 2.2 - Thema 2
        public static class TasksBuilder {

            private String task;
            private boolean checked;

            private int id;

            public TasksBuilder(String task, boolean checked){
                this.task = task;
                this.checked = checked;
            }

            public TasksBuilder id(int id){
                this.id = id;
                return this;
            }

            public Tasks build(){
                return new Tasks(this);
            }
        }

        private Tasks (TasksBuilder builder){
            task = builder.task;
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
