package controller;

import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;
import de.ur.mi.pluginhelper.logger.LogDataType;
import gui.NoteAddingFrame;
import gui.PluginWebViewFXPanel;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import javafx.stage.FileChooser;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import toolWindow.HandoutContentScreen;
import toolWindow.actions.HandoutDownloadAction;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Objects;

import static environment.FileConstants.HANDOUT_PDF_FILE_NAME;
import static environment.FileConstants.URL_BEGIN_FOR_FILE;
import static environment.Messages.FILES_SELECTING_DESCRIPTION;
import static environment.Messages.FILES_SELECTING_TEXT;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public class DownloadPDFHelper {



    boolean success;

    private static DownloadPDFHelper single_instance = null;
    //private HandoutContentScreen handoutContentScreen;

    public static DownloadPDFHelper getInstance() {
        if (single_instance == null) {
            single_instance = new DownloadPDFHelper();
        }
        System.out.println("DownloadPDFHelper getInstance");
        return single_instance;
    }

    private DownloadPDFHelper(){
        System.out.println("DownloadPDFHelper");
    }

    public void setContent(HandoutContentScreen handoutContentScreen){
        //this.handoutContentScreen = handoutContentScreen;
    }

    //called from html
    public void downloadHandout() {
        System.out.println("downloadHandout");
        String htmlDirectory = RepoLocalStorageDataProvider.getHandoutHtmlString();
        //TODO: if no content data is available
        System.out.println(htmlDirectory);
        File content = LocalStorageDataProvider.getHandoutFileDirectory();
        try {
            String urlString = content.toURI().toURL().toString();
            System.out.println(urlString);
            //https://www.mkyong.com/swing/java-swing-jfilechooser-example/
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle(FILES_SELECTING_TEXT);
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = jfc.showSaveDialog(null);
            // int returnValue = jfc.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                String handoutPDFDirectory = selectedFile.getPath() + HANDOUT_PDF_FILE_NAME;
                //https://github.com/wooio/htmltopdf-java
                success = HtmlToPdf.create()
                        .object(HtmlToPdfObject.forUrl(URL_BEGIN_FOR_FILE + htmlDirectory))
                        .convert(handoutPDFDirectory);
                showDownloadDialog();
                return;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void showDownloadDialog() {
        System.out.println(" showDownloadDialog");
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