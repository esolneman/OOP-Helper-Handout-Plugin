package controller;

import de.ur.mi.pluginhelper.logger.LogDataType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.web.WebView;

public class LoggingWebViewController {

    private final WebView webView;
    private LogDataType logDataType;

    //https://stackoverflow.com/questions/49509395/synchronize-scrollbars-of-two-javafx-webviews
    public LoggingWebViewController(WebView webView, LogDataType logDataType){
        this.webView = webView;
        this.logDataType = logDataType;
    }

    public void addLoggingKeyEvents() {
        webView.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            LoggingController.getInstance().saveDataInLogger(logDataType, "KEY_RELEASED", e.getCode().toString());
        });

    }

    public void addLoggingMouseEvents() {
        webView.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            LoggingController.getInstance().saveDataInLogger(logDataType, "MOUSE_Event", "MOUSE_ENTERED");
        });

        webView.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            LoggingController.getInstance().saveDataInLogger(logDataType, "MOUSE_Event", "MOUSE_EXITED");
        });

        webView.addEventHandler(ScrollEvent.ANY, e -> {
            LoggingController.getInstance().saveDataInLogger(logDataType, "Scroll Event", e.getEventType().getName());
        });

        webView.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            LoggingController.getInstance().saveDataInLogger(logDataType, "MOUSE_DRAGGED Event", e.getEventType().getName());
        });
    }
}
