package controller;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;

public class BalloonPopupController {

    public BalloonPopupController(){}

    public static void createBalloonNotification(JComponent component, Balloon.Position position, String notificationText, MessageType messageType){
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(notificationText, messageType, null)
                .setFadeoutTime(7500)
                .setSmallVariant(true)
                .createBalloon()
                .show(RelativePoint.getNorthEastOf(component), position);
    }
}
