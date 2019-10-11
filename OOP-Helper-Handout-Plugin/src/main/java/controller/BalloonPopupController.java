package controller;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;

public class BalloonPopupController {

    public BalloonPopupController(){}

    public void createBalloonNotification(JComponent component, Balloon.Position position, String notificationText, MessageType messageType){
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(notificationText, messageType, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(component),
                        position);
    }

}
