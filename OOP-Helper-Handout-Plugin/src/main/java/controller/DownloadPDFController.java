package controller;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.vfs.VirtualFile;
import de.ur.mi.pluginhelper.logger.LogDataType;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import provider.RepoLocalStorageDataProvider;

import java.util.Objects;

import static environment.FileConstants.HANDOUT_PDF_FILE_NAME;
import static environment.FileConstants.URL_BEGIN_FOR_FILE;
import static environment.LoggingMessageConstants.*;
import static environment.Messages.*;

public class DownloadPDFController {

    boolean convertHandoutToPDFSuccess;

    private static DownloadPDFController single_instance = null;

    public static DownloadPDFController getInstance() {
        if (single_instance == null) {
            single_instance = new DownloadPDFController();
        }
        return single_instance;
    }

    private DownloadPDFController() {
    }


    //called from html download Handout as PDF in directory
    public void downloadHandout() {
        LoggingController.getInstance().saveDataInLogger(LogDataType.HANDOUT, DOWNLOAD_PDF, OPEN_FILE_CHOOSER);
        ApplicationManager.getApplication().invokeLater(() -> {
            String handoutHTMLDirectory = RepoLocalStorageDataProvider.getHandoutHtmlString();
            Project project = RepoLocalStorageDataProvider.getProject();
            //file chooser to choose destination for the downloaded handout
            final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
            descriptor.setTitle(FILES_SELECTING_TEXT);
            descriptor.setDescription(FILES_SELECTING_DESCRIPTION);
            descriptor.setForcedToUseIdeaFileChooser(true);
            VirtualFile file = FileChooser.
                    chooseFile(descriptor, project, null);
            if (!Objects.isNull(file)) {
                convertHandoutToPDF(file, handoutHTMLDirectory);
            }
        });
    }

    private void convertHandoutToPDF(VirtualFile file, String handoutHTMLDirectory){
        //https://github.com/wooio/htmltopdf-java
        String handoutPDFDirectory = file.getPath() + HANDOUT_PDF_FILE_NAME;
        convertHandoutToPDFSuccess = HtmlToPdf.create()
                .object(HtmlToPdfObject.forUrl(URL_BEGIN_FOR_FILE + handoutHTMLDirectory))
                .convert(handoutPDFDirectory);
        showDownloadDialog();
    }

    //show notification about download status and log the event
    private void showDownloadDialog() {
        ApplicationManager.getApplication().invokeLater(() -> {
            if (convertHandoutToPDFSuccess) {
                BalloonPopupController.showBalloonNotification(Balloon.Position.above, DOWNLOAD_SUCCESS_MESSAGE, FILE_CHOOSER_TITLE, MessageType.INFO);
                LoggingController.getInstance().saveDataInLogger(LogDataType.HANDOUT, DOWNLOAD_PDF, PDF_DOWNLOAD_SUCCESS);
            } else {
                BalloonPopupController.showBalloonNotification(Balloon.Position.above, DOWNLOAD_ERROR_MESSAGE, FILE_CHOOSER_TITLE, MessageType.ERROR);
                LoggingController.getInstance().saveDataInLogger(LogDataType.HANDOUT, DOWNLOAD_PDF, PDF_DOWNLOAD_ERROR);
            }
        });

    }
}