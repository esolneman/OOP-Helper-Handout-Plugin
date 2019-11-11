package objects;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Checklist {

    private ArrayList<Tasks> tasks;

    public static class Tasks {
        private String task;
        private ArrayList<String> childTasks;

        public String getTask(){
            return task;
        }

        public void setTask(String task){
            this.task = task;
        }

        public ArrayList<String> getChildTasks(){
            return childTasks;
        }

        public void setChildTask(ArrayList<String> childTasks){
            this.childTasks = childTasks;
        }
    }

    public ArrayList<Tasks> getTasks(){
        return tasks;
    }

    public void setTasks(ArrayList<Tasks>  tasks){
        this.tasks = tasks;
    }

}
