package controller;

import de.ur.mi.pluginhelper.logger.LogDataType;
import javafx.scene.input.*;
import javafx.scene.web.WebView;

import static environment.LoggingMessageConstants.*;

public class LoggingWebViewController {

    private final WebView webView;
    private LogDataType logDataType;

    //https://stackoverflow.com/questions/49509395/synchronize-scrollbars-of-two-javafx-webviews
    public LoggingWebViewController(WebView webView, LogDataType logDataType){
        this.webView = webView;
        this.logDataType = logDataType;
    }

    public void addLoggingKeyEvents() {
        webView.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            String keyCode = e.getCode().toString();
            final KeyCombination copyCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
            if(keyCode.equals("SPACE") || keyCode.equals("PAGE_UP") || keyCode.equals("PAGE_DOWN")) {
                LoggingController.getInstance().saveDataInLogger(logDataType, KEY_EVENT, e.getCode().toString());
            } else if (copyCombination.match(e)) {
                System.out.println("COPY Combi");
                LoggingController.getInstance().saveDataInLogger(logDataType, KEY_EVENT, "Copy");
            }
        });

    }

    public void addLoggingMouseEvents() {
        webView.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            LoggingController.getInstance().saveDataInLogger(logDataType, MOUSE_EVENT, MOUSE_ENTERED);
        });

        webView.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            LoggingController.getInstance().saveDataInLogger(logDataType, MOUSE_EVENT, MOUSE_EXITED);
        });

        webView.addEventHandler(ScrollEvent.ANY, e -> {
            LoggingController.getInstance().saveDataInLogger(logDataType, SCROLL_EVENT, e.getEventType().getName());
        });

        webView.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            LoggingController.getInstance().saveDataInLogger(logDataType, MOUSE_DRAGGED_EVENT, e.getEventType().getName());
        });
    }
}
