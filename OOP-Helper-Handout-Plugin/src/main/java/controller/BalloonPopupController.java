package controller;

import com.intellij.notification.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

import static java.nio.charset.StandardCharsets.*;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BalloonPopupController {

    //https://stackoverflow.com/a/32928915
    public static final NotificationGroup OOP_HELPER_HANDOUT_NOTIFICATION = new NotificationGroup("OOP-Helper-Handout", NotificationDisplayType.BALLOON, true);

    public static void showBalloonNotification(JComponent component, Balloon.Position position, String notificationText, MessageType messageType) {
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(notificationText, messageType, null)
                .setFadeoutTime(8000)
                //.setDialogMode(true)
                //TODO: add Title as param
                .setTitle("Download Information")
                .setSmallVariant(false)
                .createBalloon()
                .show(RelativePoint.getNorthEastOf(component), position);
    }

    //https://stackoverflow.com/a/32928915
    public static void showNotification(Project project, String message, NotificationType messageType) {
        ApplicationManager.getApplication().invokeLater(() -> {
            //TODO Subtitle and Title as param
            // https://stackoverflow.com/a/20243062
            byte[] ptext = message.getBytes(ISO_8859_1);
            String encodedMessage = new String(ptext, UTF_8);
            Notification notification = OOP_HELPER_HANDOUT_NOTIFICATION.createNotification("Content Data INromation", "Subtitle", encodedMessage, messageType);
            Notifications.Bus.notify(notification, project);
        });
    }

    public static void showDialog(Project project, String message, NotificationType messageType) {
        ApplicationManager.getApplication().invokeLater(() -> {
            Notification notification = OOP_HELPER_HANDOUT_NOTIFICATION.createNotification("Content Data INromation", "Subtitle", message, messageType);
            Notifications.Bus.notify(notification, project);
        });
    }
}
