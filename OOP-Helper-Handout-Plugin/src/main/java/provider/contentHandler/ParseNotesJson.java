package provider.contentHandler;

import com.google.gson.*;
import objects.Notes;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;

import java.io.*;
import java.util.ArrayList;

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

    public static Notes getNotesFromJsonObject(JsonObject notesJsonObject) {
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

    public static Element getTableFromNotes(File htmlFile, Notes notes) throws IOException {
        //https://stackoverflow.com/a/30258688
        Document jsoupDoc = Jsoup.parse(htmlFile, "UTF-8");
        W3CDom documentJava = new W3CDom();
        org.w3c.dom.Document w3cDoc = documentJava.fromJsoup(jsoupDoc);
        //TODO Sometimes Nullpointer
        org.w3c.dom.Element ele = w3cDoc.getElementById("notesTable");


        /*
        ele.appendChild(TableRow)
        parentElement.insertBefore(mark, ele);
        mark.appendChild(ele);
        webView.getEngine().load(newLocation);
        */
         return null;
    }

}
