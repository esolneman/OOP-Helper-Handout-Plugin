package provider.contentHandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import objects.Notes;

public class ParseNotesJson {

    public static JsonObject getJsonObjectFromNotes(Notes notes){
        JsonObject notesJsonObject = new JsonObject();
        JsonArray notesJson = new JsonArray();
        notesJsonObject.add("notes", notesJson);
        JsonObject note;
        for (int i = 0; i < notes.notes.size(); i++) {
            note = new JsonObject();
            note.addProperty("note", notes.notes.get(i).note);
            note.addProperty("date", notes.notes.get(i).date.toString());
            notesJson.add(note);
        }
        return notesJsonObject;
    }

}
