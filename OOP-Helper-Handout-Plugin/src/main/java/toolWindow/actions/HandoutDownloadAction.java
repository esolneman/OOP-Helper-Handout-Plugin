package toolWindow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

import static environment.Constants.LOCAL_STORAGE_FILE;
import static environment.Constants.REPO_LOCAL_STORAGE_FILE;

public class HandoutDownloadAction extends AnAction {

    public static final String DEST = "target/results/sample.pdf";
    private Project project;

    public HandoutDownloadAction() {
        super("Download");
    }

    public void actionPerformed(AnActionEvent event) {
        System.out.println("DownloadAction started");
        System.out.println(event.getPlace());
        project = event.getProject();
        String handoutHTMLDirectory = RepoLocalStorageDataProvider.getHandoutHtmlString();
        create (handoutHTMLDirectory);
    }

    //https://github.com/wooio/htmltopdf-java
    private void create(String handoutHTMLDirectory) {
        System.out.println(handoutHTMLDirectory);
        File content = LocalStorageDataProvider.getHandoutFileDirectory();
        try {
            String urlString = content.toURI().toURL().toString();
            System.out.println(urlString);
            final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
            descriptor.setTitle("Choose location for PDF");
            descriptor.setDescription("Description for Downloading PDF");
            descriptor.setForcedToUseIdeaFileChooser(true);
            VirtualFile file = FileChooser
                    .chooseFile(descriptor, project, null);
            String handoutPDFDirectory = file.getPath() + "/handout.pdf";
            System.out.println("handoutPDFDirectory: " + handoutPDFDirectory);
            boolean success = HtmlToPdf.create()
                    .object(HtmlToPdfObject.forUrl("file:///" + handoutHTMLDirectory))
                    .convert(handoutPDFDirectory);
            System.out.println(success);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
