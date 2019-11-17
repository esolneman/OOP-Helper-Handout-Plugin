package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import objects.Notes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import provider.LocalStorageDataProvider;

import java.io.*;
import java.util.Date;

//TODO LET CONTROLLER CONTROLL
public class NotesController {


    public static void createNotesFile(){

    }

    public static void saveNewEntryInFile(String htmlText){
        Document doc = Jsoup.parse(htmlText);
        String htmlBody = doc.body().html();
        System.out.println("htmlBody: " + htmlBody);

        //create new Note
        Notes.Note newNote = new Notes.Note();
        newNote.note = htmlBody;
        newNote.date = new Date().toString();
        //Notes.Note newNote = ParseNotesJson.getNoteFromString(htmlText);
        File notesFile = LocalStorageDataProvider.getNotesFile();

        //TODO ADD SOURCE
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(notesFile));
            Notes allNotes = gson.fromJson(reader, Notes.class);
            if(allNotes != null){
                allNotes.notes.add(newNote);
            }
            saveNotesInFile(allNotes,notesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void saveJsonObjectInFile(JsonObject jsonObject, File file) {
        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveNotesInFile(Notes notes, File file) {
        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(notes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createHTMLString(Notes notes, File initFile) throws IOException {
        System.out.println("initFile: " + initFile.getPath());

        System.out.println("notes: " + notes.notes.get(0).note);


        //https://stackoverflow.com/a/30258688
        Document jsoupDoc = Jsoup.parse(initFile, "UTF-8");

        System.out.println("jsoupDoc: " + jsoupDoc.toString());


        //TODO Sometimes Nullpointer
        Element ele = jsoupDoc.getElementById("notesTableBody");
        System.out.println("ele: " + ele.toString());

        for (Notes.Note note : notes.notes) {
            System.out.println("note: " + note.note);
            Element row = jsoupDoc.createElement("tr");
            row.text(note.note);
            ele.appendChild(row);
        }
        System.out.println("jsoupDoc row: " + jsoupDoc);

        return jsoupDoc.html();
    }
}
