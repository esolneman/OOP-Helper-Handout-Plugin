package controller;

import com.intellij.notification.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import provider.RepoLocalStorageDataProvider;

import static java.nio.charset.StandardCharsets.*;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BalloonPopupController {

    //https://stackoverflow.com/a/32928915
    public static final NotificationGroup OOP_HELPER_HANDOUT_NOTIFICATION = new NotificationGroup("OOP-Helper-Handout", NotificationDisplayType.BALLOON, false);

    public static void showBalloonNotification( Balloon.Position position, String notificationText, String title, MessageType messageType) {
        byte[] ptext = notificationText.getBytes(ISO_8859_1);
        String encodedNotificationText = new String(ptext, UTF_8);

        ptext = title.getBytes(ISO_8859_1);
        String encodedTitle = new String(ptext, UTF_8);

        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(encodedNotificationText, messageType, null)
                .setFadeoutTime(6000)
                //.setDialogMode(true)
                //TODO: add Title as param
                .setTitle(encodedTitle)
                .setSmallVariant(false)
                .createBalloon()
                .show(RelativePoint.getNorthEastOf(WindowManager.getInstance().getAllProjectFrames()[0].getComponent()), position);
    }

/*    //https://stackoverflow.com/a/32928915
    public static void showNotification(Project project, String message, NotificationType messageType) {
        ApplicationManager.getApplication().invokeLater(() -> {
            //TODO Subtitle and Title as param
            // https://stackoverflow.com/a/20243062
            byte[] ptext = message.getBytes(ISO_8859_1);
            String encodedMessage = new String(ptext, UTF_8);
            Notification notification = OOP_HELPER_HANDOUT_NOTIFICATION.createNotification("Status der Inhalte", "", encodedMessage, messageType);
            Notifications.Bus.notify(notification, project);
        });
    }*/
}
