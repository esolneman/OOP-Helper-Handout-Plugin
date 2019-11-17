package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import objects.Notes;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import provider.LocalStorageDataProvider;

import java.io.*;
import java.util.Date;

//TODO LET CONTROLLER CONTROLL
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

        //TODO ADD SOURCE
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

    public static File createHTMLString(Notes notes, File initFile) throws IOException {
        System.out.println("initFile: " + initFile.getPath());



        //https://stackoverflow.com/a/30258688
        Document jsoupDoc = Jsoup.parse(initFile, "UTF-8");

        System.out.println("jsoupDoc: " + jsoupDoc.toString());


        W3CDom documentJava = new W3CDom();
        org.w3c.dom.Document w3cDoc = documentJava.fromJsoup(jsoupDoc);


        System.out.println("w3cDoc: " + w3cDoc.toString());

        System.out.println("w3cDoc: " + w3cDoc.getElementsByTagName("table"));
        //TODO Sometimes Nullpointer
        org.w3c.dom.Element ele = (Element) w3cDoc.getElementsByTagName("table").item(0);
        System.out.println("ele: " + ele.toString());

        for (Notes.Note note : notes.notes) {
            System.out.println("note: " + note.note);
            Element row = w3cDoc.createElement("tr");
            row.setTextContent(note.note);
            ele.appendChild(row);
        }
        System.out.println("w3cDoc row: " + w3cDoc.getElementsByTagName("table").item(0).getChildNodes().item(0).toString());

        return initFile;
    }
}
