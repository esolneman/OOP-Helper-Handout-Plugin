package controller;

import objects.Notes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import provider.LocalStorageDataProvider;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import static environment.Messages.INITIAL_NOTES_TEXT;

//TODO LET CONTROLLER CONTROLL
public class NotesController {

    private static NotesController single_instance = null;
    private File notesLocalFile;

    public static NotesController getInstance() {
        if (single_instance == null) {
            single_instance = new NotesController();
        }
        return single_instance;
    }

    private NotesController(){
        notesLocalFile = LocalStorageDataProvider.getNotesFile();
    }

    public void createNotesFile() {
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

    //TODO save
    public static void saveNewEntryInFile(String htmlText) {
        Document doc = Jsoup.parse(htmlText);
        String htmlBody = doc.getElementById("notesList").html();
        //create new Note
        //TODO DO I NEED THIS??
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
        jsoupDoc.getElementById("notesList").html(htmlBody);

        //https://www.baeldung.com/java-write-to-file#write-with-printwriter
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(initFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(jsoupDoc);
        printWriter.close();
    }

    public Document getCurrentNotesDocument() {
        //https://jsoup.org/cookbook/input/parse-document-from-string
        Document doc = null;
        try {
            doc = Jsoup.parse(notesLocalFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
