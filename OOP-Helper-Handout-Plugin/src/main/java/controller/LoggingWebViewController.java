package controller;

import de.ur.mi.pluginhelper.logger.LogDataType;
import javafx.scene.input.*;
import javafx.scene.web.WebView;

import static environment.LoggingMessageConstants.*;

//controller class to log user interaction in WebViews
public class LoggingWebViewController {

    private final WebView webView;
    private LogDataType logDataType;
    private static String KEY_CODE_SPACE = "SPACE";
    private static String KEY_CODE_PAGE_UP = "PAGE_UP";
    private static String KEY_CODE_PAGE_DOWN = "PAGE_DOWN";


    //https://stackoverflow.com/questions/49509395/synchronize-scrollbars-of-two-javafx-webviews
    public LoggingWebViewController(WebView webView, LogDataType logDataType){
        this.webView = webView;
        this.logDataType = logDataType;
    }

    public void addLoggingKeyEvents() {
        webView.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            String keyCode = e.getCode().toString();
            final KeyCombination copyCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
            if(keyCode.equals(KEY_CODE_SPACE) || keyCode.equals(KEY_CODE_PAGE_UP) || keyCode.equals(KEY_CODE_PAGE_DOWN)) {
                LoggingController.getInstance().saveDataInLogger(logDataType, KEY_EVENT, e.getCode().toString());
            } else if (copyCombination.match(e)) {
                LoggingController.getInstance().saveDataInLogger(logDataType, KEY_EVENT, COPY_EVENT);
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
