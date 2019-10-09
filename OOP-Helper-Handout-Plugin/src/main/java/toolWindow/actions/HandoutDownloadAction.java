package toolWindow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.sun.org.apache.xpath.internal.operations.Div;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import org.jsoup.Jsoup;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;

import static environment.Constants.LOCAL_STORAGE_FILE;
import static environment.Constants.REPO_LOCAL_STORAGE_FILE;

public class HandoutDownloadAction extends AnAction {

    public static final String DEST = "target/results/sample.pdf";

    public HandoutDownloadAction() {
        super("Download");
    }

    public void actionPerformed(AnActionEvent event) {
        System.out.println("DownloadAction started");
        String handoutHTMLDirectory = RepoLocalStorageDataProvider.getHandoutHtmlString();
        String handoutPDFDirectory = RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + "/handout.pdf";
        create (handoutHTMLDirectory, handoutPDFDirectory);
    }

    private void create(String handoutHTMLDirectory, String handoutPDFDirectory) {
        System.out.println(handoutHTMLDirectory);
        File content = LocalStorageDataProvider.getHandoutFileDirectory();
        try {
            String urlString = content.toURI().toURL().toString();
            System.out.println(urlString);
            boolean success = HtmlToPdf.create()
                    .object(HtmlToPdfObject.forUrl("file:///" + handoutHTMLDirectory))
                    .convert(handoutPDFDirectory);
            System.out.println(success);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }


}
