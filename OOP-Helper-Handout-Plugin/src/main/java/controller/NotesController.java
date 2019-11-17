package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import objects.Notes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import provider.contentHandler.ParseNotesJson;

import java.io.*;
import java.util.Date;

public class NotesController {


    public static void createNotesFile(){

    }

    public static void saveNewEntryInFile(String htmlText){
        System.out.println("HTMLTEXT: " + htmlText);
        Document doc = Jsoup.parse(htmlText);
        String htmlBody = doc.body().toString();
        System.out.println("htmlBody: " + htmlBody);

        //create new Note
        Notes.Note newNote = new Notes.Note();
        newNote.note = htmlBody;
        newNote.date = new Date().toString();
        //Notes.Note newNote = ParseNotesJson.getNoteFromString(htmlText);
        File notesFile = LocalStorageDataProvider.getNotesFile();

        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(notesFile));
            Notes allNotes = gson.fromJson(reader, Notes.class);
            allNotes.notes.add(newNote);
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

}
