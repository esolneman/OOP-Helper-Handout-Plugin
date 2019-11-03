package controller;

import com.intellij.notification.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;

public class BalloonPopupController {

    //https://stackoverflow.com/a/32928915
    public static final NotificationGroup OOP_HELPER_HANDOUT_NOTIFICATION = new NotificationGroup("OOP-Helper-Handout", NotificationDisplayType.BALLOON, true);

    public static void createBalloonNotification(JComponent component, Balloon.Position position, String notificationText, MessageType messageType){
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(notificationText, messageType, null)
                .setFadeoutTime(7500)
                .setSmallVariant(true)
                .createBalloon()
                .show(RelativePoint.getNorthEastOf(component), position);
    }

    //https://stackoverflow.com/a/32928915
    public static void showMyMessage(Project project, String message) {
        ApplicationManager.getApplication().invokeLater(() -> {
            Notification notification = OOP_HELPER_HANDOUT_NOTIFICATION.createNotification(message, NotificationType.ERROR);
            Notifications.Bus.notify(notification, project);
        });
    }
}
