package controller;

import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import provider.LocalStorageDataProvider;
import java.io.*;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

//TODO LET CONTROLLER CONTROLL
public class NotesController {

    private static NotesController single_instance = null;
    private File notesLocalFile;
    private WebView webView;

    public static NotesController getInstance() {
        if (single_instance == null) {
            single_instance = new NotesController();
        }
        return single_instance;
    }

    private NotesController() {
        notesLocalFile = LocalStorageDataProvider.getNotesFile();
    }

    //TODO CALL CREATE FILE CLASS
    public void createNotesFile() {
        notesLocalFile.getParentFile().mkdirs();
        try {
            notesLocalFile.createNewFile();
        } catch (IOException e) {
            //TODO CATACH
            System.out.println(e);
        }
        File notesRepoFile = LocalStorageDataProvider.getInitNotesHtmlFile();
        CreateFiles.saveRepoFileInLocalFile(notesRepoFile, notesLocalFile);
    }

    public void setWebView(WebView webView) {
        System.out.println(" setWebView WebView: " + webView);
        this.webView = webView;
    }

    public void saveNewEntryInFile(String htmlText) {
        //TODO already false encoded
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(System.out, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        printStream.println("saveNewEntryInFile htmlText: " + htmlText);
        //System.out.println("saveNewEntryInFile htmlText: " + htmlText);
        //TODO Subtitle and Title as param
        // https://stackoverflow.com/a/20243062
        byte[] ptext = htmlText.getBytes(ISO_8859_1);
        String encodedMessage = new String(ptext, UTF_8);
        //System.out.println("saveNewEntryInFile encodedMessage: " + encodedMessage);
        printStream.println("saveNewEntryInFile encodedMessage: " + encodedMessage);

        // TODO CHANGE TO W3 DOCUJMENT -> no pass with utf 8
        try {
            org.jsoup.nodes.Document doc = Jsoup.parse(htmlText, "UTF-8");
            printStream.println("saveNewEntryInFile Document body save: " + doc.body().toString());
            //System.out.println("Document body save: " + doc.body().toString());
            String htmlBody = doc.body().html();
            printStream.println("saveNewEntryInFile htmlBody: " +htmlBody);
            //System.out.println("saveNewEntryInFile htmlBody: " + htmlBody);
            saveNoteInHtmlFile(htmlBody, LocalStorageDataProvider.getNotesFile());
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    //TODO add to File Controller
    private void saveNoteInHtmlFile(String htmlBody, File initFile) throws IOException {
        //https://stackoverflow.com/a/30258688
       // Document doc = webView.getEngine().getDocument();
        //doc.getElementById("notesList").setTextContent(htmlBody);
        //TODO UTF ( encoding
        org.jsoup.nodes.Document doc = Jsoup.parse(initFile, "UTF-8");
        doc.getElementById("notesList").html(htmlBody);


        //https://stackoverflow.com/a/1001568
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(initFile), "UTF-8"));
        try {
            out.write(String.valueOf(doc));
        } finally {
            out.close();
        }


/*
        //https://www.baeldung.com/java-write-to-file#write-with-printwriter
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(initFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(doc);
        printWriter.close();*/
    }

    public Document getCurrentNotesDocument() {
        /*
        //https://jsoup.org/cookbook/input/parse-document-from-string
        Document doc = null;
        try {
            doc = Jsoup.parse(notesLocalFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Document doc = webView.getEngine().getDocument();
        return doc;
    }

    public WebView getCurrentWebview() {
        return webView;
    }
}
