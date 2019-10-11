package toolWindow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.*;
import com.intellij.ui.awt.RelativePoint;
import controller.BalloonPopupController;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import toolWindow.HandoutContentScreen;

import javax.swing.*;
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
        create (handoutHTMLDirectory, event);
    }

    //https://github.com/wooio/htmltopdf-java
    /*

     */
    private void create(String handoutHTMLDirectory, AnActionEvent event) {
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
            //JComponent handoutContentScreenToolbar = new HandoutContentScreen().getToolbar();
            JComponent handoutContentScreen = ToolWindowManager.getActiveToolWindow().getComponent();
            BalloonPopupController test = new BalloonPopupController();
            if(success){
                test.createBalloonNotification(handoutContentScreen, Balloon.Position.above, "Downloading was successfully", MessageType.INFO);
            }else{
                test.createBalloonNotification(handoutContentScreen, Balloon.Position.above, "Error while downloading the handout. Please try again.", MessageType.ERROR);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
