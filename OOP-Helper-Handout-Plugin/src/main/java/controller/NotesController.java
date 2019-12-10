package controller;

import javafx.scene.web.WebView;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import provider.LocalStorageDataProvider;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

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
        // TODO CHANGE TO W3 DOCUJMENT
        DocumentBuilder db = null;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(htmlText));
            System.out.println("htmlText save: " + htmlText);
            System.out.println("InputSource save: " + is);
            System.out.println("db save: " + db);
           // Document doc = db.parse(is);

            HtmlCleaner cleaner = new HtmlCleaner();
            TagNode node = cleaner.clean(htmlText);
            DomSerializer ser = new DomSerializer(cleaner.getProperties());
            //Document doc = ser.createDOM(node);

            org.jsoup.nodes.Document doc = Jsoup.parse(htmlText);
            System.out.println("Document save: " + doc.toString());
            System.out.println("Document body save: " + doc.body().toString());

            String htmlBody = doc.body().html();


            System.out.println("doc save: " + doc);

            //String htmlBody = doc.getElementById("notesList").toString();
            saveNoteInHtmlFile(htmlBody, LocalStorageDataProvider.getNotesFile());
        } catch (ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }

        //create new Note
        //TODO DO I NEED THIS??
/*        Notes.Note newNote = new Notes.Note();
        newNote.note = htmlBody;
        newNote.date = new Date().toString();*/
    }

    //TODO add to File Controller
    private void saveNoteInHtmlFile(String htmlBody, File initFile) throws IOException {
        //https://stackoverflow.com/a/30258688
       // Document doc = webView.getEngine().getDocument();
        //doc.getElementById("notesList").setTextContent(htmlBody);
        org.jsoup.nodes.Document doc = Jsoup.parse(initFile, "UTF-8");
        doc.getElementById("notesList").html(htmlBody);
        //https://www.baeldung.com/java-write-to-file#write-with-printwriter
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(initFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(doc);
        printWriter.close();
    }

    public Document getCurrentNotesDocument() {
        //https://jsoup.org/cookbook/input/parse-document-from-string
        /*        Document doc = null;
        try {
            doc = Jsoup.parse(notesLocalFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("WebView: " + webView.getEngine().getDocument().getDocumentURI());
        Document doc = webView.getEngine().getDocument();
        return doc;
    }

    public WebView getCurrentWebview() {
        return webView;
    }
}
