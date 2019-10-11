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
import java.io.File;
import java.net.MalformedURLException;

import static environment.Constants.*;
import static environment.Messages.FILES_SELECTING_DESCRIPTION;
import static environment.Messages.FILES_SELECTING_TEXT;

public class HandoutDownloadAction extends AnAction {

    public static final String DEST = "target/results/sample.pdf";
    private Project project;

    public HandoutDownloadAction() {
        super("Download");
    }

    public void actionPerformed(AnActionEvent event) {
        project = event.getProject();
        String handoutHTMLDirectory = RepoLocalStorageDataProvider.getHandoutHtmlString();
        create (handoutHTMLDirectory);
    }

    //https://github.com/wooio/htmltopdf-java
    /*

     */
    private void create(String handoutHTMLDirectory) {
        System.out.println(handoutHTMLDirectory);
        File content = LocalStorageDataProvider.getHandoutFileDirectory();
        try {
            String urlString = content.toURI().toURL().toString();
            System.out.println(urlString);
            //https://www.programcreek.com/java-api-examples/?api=com.intellij.openapi.fileChooser.FileChooserDescriptor
            final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
            descriptor.setTitle(FILES_SELECTING_TEXT);
            descriptor.setDescription(FILES_SELECTING_DESCRIPTION);
            descriptor.setForcedToUseIdeaFileChooser(true);
            VirtualFile file = FileChooser.chooseFile(descriptor, project, null);
            String handoutPDFDirectory = file.getPath() + HANDOUT_PDF_FILE_NAME;
            boolean success = HtmlToPdf.create()
                    .object(HtmlToPdfObject.forUrl(URL_BEGIN_FOR_FILE + handoutHTMLDirectory))
                    .convert(handoutPDFDirectory);
            // TODO POPUP NOTIFICATION
            if(success){

            }else{

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
