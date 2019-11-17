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
import provider.contentHandler.ParseNotesJson;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

//TODO LET CONTROLLER CONTROLL
public class NotesController {


    //TODO
    public static void createNotesFile() {

    }

    public static void saveNewEntryInFile(String htmlText) {
        Document doc = Jsoup.parse(htmlText);
        String htmlBody = doc.body().html();
        System.out.println("htmlBody: " + htmlBody);

        //create new Note
        Notes.Note newNote = new Notes.Note();
        newNote.note = htmlBody;
        newNote.date = new Date().toString();

        try {
            saveNoteInHtmlFile(newNote, LocalStorageDataProvider.getInitNotesHtmlFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Notes.Note newNote = ParseNotesJson.getNoteFromString(htmlText);
/*        File notesFile = LocalStorageDataProvider.getNotesFile();

        //TODO ADD SOURCE
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(notesFile));
            Notes allNotes = gson.fromJson(reader, Notes.class);
            if (allNotes != null) {
                allNotes.notes.add(newNote);
            } else {
                ArrayList<Notes.Note> newNoteArrayLit = new ArrayList<>();
                newNoteArrayLit.add(newNote);
                allNotes = new Notes(newNoteArrayLit);
            }
            JsonObject notes = ParseNotesJson.getJsonObjectFromNotes(allNotes);
            //saveNotesInFile(allNotes,notesFile);
            saveJsonObjectInFile(notes, notesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    private static void saveJsonObjectInFile(JsonObject jsonObject, File file) {
        //https://www.baeldung.com/java-write-to-file#write-with-printwriter
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(jsonObject);
        printWriter.close();
    }

    public static void saveNoteInHtmlFile(Notes.Note note, File initFile) throws IOException {
        //https://stackoverflow.com/a/30258688
        Document jsoupDoc = Jsoup.parse(initFile, "UTF-8");
        
        //TODO Sometimes Nullpointer
        Element ele = jsoupDoc.getElementById("notesList");
        Element divNote = jsoupDoc.createElement("div");
        divNote.attr("id", "note");
        ele.appendChild(divNote);
        //https://stackoverflow.com/a/37277534
        //unescape-html-character-entities
        divNote.text(Jsoup.parse(note.note).text());

        //https://www.baeldung.com/java-write-to-file#write-with-printwriter
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(initFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(jsoupDoc.html());
        printWriter.close();
    }
}
