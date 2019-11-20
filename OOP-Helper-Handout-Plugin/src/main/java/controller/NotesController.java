package controller;

import javafx.embed.swing.JFXPanel;
import objects.Notes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import provider.LocalStorageDataProvider;

import java.io.*;
import java.util.Date;

//TODO LET CONTROLLER CONTROLL
public class NotesController {

    private static NotesController single_instance = null;

    public static NotesController getInstance() {
        if (single_instance == null) {
            single_instance = new NotesController();
        }
        return single_instance;
    }

    private NotesController(){}

    public static void createNotesFile() {

        File notesLocalFile = LocalStorageDataProvider.getNotesFile();
        notesLocalFile.getParentFile().mkdirs();
        try {
            notesLocalFile.createNewFile();
        } catch (IOException e) {
            //TODO CATACH
            System.out.println(e);
        }

        File notesRepoFile = LocalStorageDataProvider.getInitNotesHtmlFile();
        BufferedReader inputStream = null;
        BufferedWriter outputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(
                    notesRepoFile));
            File UIFile = notesLocalFile;
            // if File doesnt exists, then create it
            if (!UIFile.exists()) {
                UIFile.createNewFile();
            }
            FileWriter filewriter = new FileWriter(UIFile.getAbsoluteFile());
            outputStream = new BufferedWriter(filewriter);
            String count;
            while ((count = inputStream.readLine()) != null) {
                outputStream.write(count);
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveNewEntryInFile(String htmlText) {
        Document doc = Jsoup.parse(htmlText);
        String htmlBody = doc.body().html();

        //create new Note
        Notes.Note newNote = new Notes.Note();
        newNote.note = htmlBody;
        newNote.date = new Date().toString();

        try {
            saveNoteInHtmlFile(htmlBody, LocalStorageDataProvider.getNotesFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void saveNoteInHtmlFile(String htmlBody, File initFile) throws IOException {
        //https://stackoverflow.com/a/30258688
        Document jsoupDoc = Jsoup.parse(initFile, "UTF-8");
        //TODO Sometimes Nullpointer
        Element ele = jsoupDoc.getElementById("notesList");
        Element divNote = jsoupDoc.createElement("div");
        divNote.attr("id", "note");
        ele.appendChild(divNote);
        //add separator between notes
        Element separator = jsoupDoc.createElement("hr");
        ele.appendChild(separator);

        //https://stackoverflow.com/a/37277534
        //unescape-html-character-entities
        //divNote.text(Jsoup.parse(note.note).text());
        divNote.html(htmlBody);

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
