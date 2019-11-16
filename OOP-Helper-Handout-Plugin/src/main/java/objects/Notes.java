package objects;

import java.util.ArrayList;
import java.util.Date;

public class Notes {

    private ArrayList<Note> notes;

    public Notes(ArrayList<Note> notes){
        this.notes = notes;
    };

    public static class Note {
        public String note;
        public Date date;
    }
}
