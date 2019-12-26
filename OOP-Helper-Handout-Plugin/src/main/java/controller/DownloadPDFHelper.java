package controller;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;
import de.ur.mi.pluginhelper.logger.LogDataType;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import toolWindow.HandoutContentScreen;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Objects;

import static environment.FileConstants.HANDOUT_PDF_FILE_NAME;
import static environment.FileConstants.URL_BEGIN_FOR_FILE;
import static environment.Messages.FILES_SELECTING_DESCRIPTION;
import static environment.Messages.FILES_SELECTING_TEXT;

public class DownloadPDFHelper {



    boolean success;

    private static DownloadPDFHelper single_instance = null;
    //private HandoutContentScreen handoutContentScreen;

    public static DownloadPDFHelper getInstance() {
        if (single_instance == null) {
            single_instance = new DownloadPDFHelper();
        }
        return single_instance;
    }

    private DownloadPDFHelper(){
    }

    public void setContent(HandoutContentScreen handoutContentScreen){
        //this.handoutContentScreen = handoutContentScreen;
    }

    //called from html
    public void downloadHandout() {
        LoggingController.getInstance().saveDataInLogger(LogDataType.HANDOUT, "Download PDF", "open File Chooser");
        ApplicationManager.getApplication().invokeLater(() -> {
            String handoutHTMLDirectory = RepoLocalStorageDataProvider.getHandoutHtmlString();
            Project project = RepoLocalStorageDataProvider.getProject();
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
                VirtualFile file = FileChooser.
                        chooseFile(descriptor, project, null);
                if (!Objects.isNull(file)) {
                    //https://github.com/wooio/htmltopdf-java
                    String handoutPDFDirectory = file.getPath() + HANDOUT_PDF_FILE_NAME;
                    success = HtmlToPdf.create()
                            .object(HtmlToPdfObject.forUrl(URL_BEGIN_FOR_FILE + handoutHTMLDirectory))
                            .convert(handoutPDFDirectory);
                    showDownloadDialog();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    private void showDownloadDialog() {
        ApplicationManager.getApplication().invokeLater(() -> {
            JComponent handoutContentScreen = ToolWindowManager.getActiveToolWindow().getComponent();
            System.out.println(" handoutContentScreen: " + handoutContentScreen.toString());
            //TODO add Listener for this and display Notification with Listener!!!
            if (success) {
                System.out.println("SUCCESS");
                BalloonPopupController.showBalloonNotification(handoutContentScreen, Balloon.Position.above, "Downloading was successfully", MessageType.INFO);
                LoggingController.getInstance().saveDataInLogger(LogDataType.HANDOUT, "Download PDF Version", "success");
            } else {
                System.out.println("FAILURE");
                BalloonPopupController.showBalloonNotification(handoutContentScreen, Balloon.Position.above, "Error while downloading the handout. Please try again.", MessageType.ERROR);
                LoggingController.getInstance().saveDataInLogger(LogDataType.HANDOUT, "Download PDF Version", "error");
            }
        });

    }
}