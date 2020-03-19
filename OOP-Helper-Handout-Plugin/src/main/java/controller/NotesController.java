package controller;

import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import provider.LocalStorageDataProvider;
import java.io.*;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

//controller class for notes
public class NotesController {

    private static NotesController single_instance = null;
    private File notesLocalFile;
    private WebView webView;
    private String NOTES_ELEMENT_ID = "notesList";

    public static NotesController getInstance() {
        if (single_instance == null) {
            single_instance = new NotesController();
        }
        return single_instance;
    }

    private NotesController() {
        notesLocalFile = LocalStorageDataProvider.getNotesFile();
    }

    public void createNotesFile() {
        FileHandleController.createNewFile(notesLocalFile);
        File notesRepoFile = LocalStorageDataProvider.getInitNotesHtmlFile();
        FileHandleController.saveRepoFileInLocalFile(notesRepoFile, notesLocalFile);
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public void saveNewEntryInFile(String htmlText) {
        // https://stackoverflow.com/a/20243062
        try {
            org.jsoup.nodes.Document doc = Jsoup.parse(htmlText, "UTF-8");
            String htmlBody = doc.body().html();
            saveNoteInHtmlFile(htmlBody, LocalStorageDataProvider.getNotesFile());
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    private void saveNoteInHtmlFile(String htmlBody, File initFile) throws IOException {
        //https://stackoverflow.com/a/30258688
        org.jsoup.nodes.Document doc = Jsoup.parse(initFile, "UTF-8");
        doc.getElementById(NOTES_ELEMENT_ID).html(htmlBody);

        //https://stackoverflow.com/a/1001568
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(initFile), "UTF-8"));
        try {
            out.write(String.valueOf(doc));
        } finally {
            out.close();
        }
    }

    public WebView getCurrentWebview() {
        return webView;
    }
}
