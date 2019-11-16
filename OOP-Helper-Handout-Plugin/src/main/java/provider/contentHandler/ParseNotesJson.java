package provider.contentHandler;

import com.google.gson.*;
import gherkin.lexer.No;
import objects.Notes;
import provider.LocalStorageDataProvider;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ParseNotesJson {

    public static JsonObject getJsonObjectFromNotes(Notes notes){
        System.out.println("getJsonObjectFromNotes NOTE: " + notes.notes.get(0).note);

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

    public static Notes getNotesFromJsonObject(JsonObject notesJsonObject) throws ParseException {
        JsonArray notesJsonArray = ((JsonArray) notesJsonObject.get("notes"));
        ArrayList<Notes.Note> notesArray = new ArrayList<>();
        for (JsonElement note : notesJsonArray) {
            String description = note.getAsJsonObject().get("note").toString();
            String date = note.getAsJsonObject().get("date").getAsString();
            Notes.Note notet = new Notes.Note();
            notet.note = description;
            notet.date = date;
            notesArray.add(notet);
        }
        Notes notes = new Notes(notesArray);
        return notes;
    }

}
