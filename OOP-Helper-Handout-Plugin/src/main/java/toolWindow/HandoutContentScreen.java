package toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import environment.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

//import javafx.embed.swing.JFXPanel;

public class HandoutContentScreen {
    private HandoutPluginFXPanel handoutContent;
    private WebView webView;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;

    public HandoutContentScreen(ToolWindow toolWindow){
        handoutToolWindow = toolWindow;
        createContent();
        initToolWindowMenu();
        content = LocalStorageDataProvider.getHandoutFileDirectory();
        try {
            urlString = content.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void initToolWindowMenu() {
       /* ActionToolbar toolbar;
        ActionToolbar handoutToolbar;
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true) ;
        handoutToolbar.setTargetComponent(panel);
        panel.setToolbar(handoutToolbar.getComponent());
        handoutContent.add(panel);*/
         //ActionManager.getInstance().createActionToolbar();
        //com.intellij.openapi.actionSystem.ActionManager.createActionToolbar ` and add AnAction on it via ActionGroup.
        //ToolWindowActionMAnager
        /*ActionGroup handoutActionGroup = new ActionGroup() {
            @NotNull
            @Override
            public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
                return new AnAction[0];
            }
        };
        ActionManager actionManager = ActionManager.getInstance();
        actionManager.createButtonToolbar("help", handoutActionGroup );*/
        /*ActionToolbar actionToolbar = (ActionToolbar) this.handoutContent;
        actionToolbar.setOrientation(SwingConstants.HORIZONTAL);
        //handoutContent.remove();
        handoutContent.add(actionToolbar.getComponent(), BorderLayout.PAGE_START);*/
    }

    private void createContent() {
        handoutContent = new HandoutPluginFXPanel();
/*      //JPanel panel = new JPanel();

        // JButton mit Text "Drück mich" wird erstellt
        JButton button = new JButton("Drück mich");

        // JButton wird dem Panel hinzugefügt
        handoutContent.add(button);*/


        //URL url = getClass().getResource("C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/index.html");
        //String url = WebView.class.getResource(file).toExternalForm();
        Platform.runLater(() -> {
            webView = new WebView();
            webView.getEngine().load(urlString);
            //webView.getEngine().load(url.toExternalForm());
            //URL url = getClass().getResource("index.html");
            //webEngine.load(url.toExternalForm());
            webView.getEngine().setJavaScriptEnabled(true);
            handoutContent.setScene(new Scene(webView));
            setOnLinkListener();
        });
    }

    //https://stackoverflow.com/questions/15555510/javafx-stop-opening-url-in-webview-open-in-browser-instead
    //https://stackoverflow.com/questions/31909455/open-hyperlinks-in-javafx-webview-with-default-browser
    public void setOnLinkListener() {
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                Desktop d = Desktop.getDesktop();
                System.out.println("message: " + webView.getEngine().getLoadWorker().getMessage());
                String toBeopen = webView.getEngine().getLoadWorker().getMessage().trim();
                System.out.println("tobeopen: " + toBeopen);
                try {
                    URI address = new URI(observable.getValue());
                    System.out.println("address: " + address);
                    if (toBeopen.contains("http://") || toBeopen.contains("https://") || toBeopen.contains("mailto")) {
                        System.out.println("load nwe page ");
                        try {
                            Platform.setImplicitExit(false);
                            Platform.runLater(() -> {
                                webView.getEngine().load(urlString);
                            });
                            System.out.println("BrowserUtil");

                            d.browse(address);
                            //BrowserUtil.browse((address.toURL());
                        }
                        catch (IOException e) {
                            System.out.println(e);
                        }
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        });
        //https://stackoverflow.com/questions/15555510/javafx-stop-opening-url-in-webview-open-in-browser-instead
       /* Document document = webView.getEngine().getDocument();
        NodeList nodeList = document.getElementsByTagName("a");
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node node= nodeList.item(i);
            EventTarget eventTarget = (EventTarget) node;
            eventTarget.addEventListener("click", new EventListener()
            {
                @Override
                public void handleEvent(org.w3c.dom.events.Event event)
                {
                    EventTarget target = event.getCurrentTarget();
                    HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
                    String href = anchorElement.getHref();
                    //handle opening URL outside JavaFX WebView
                    System.out.println(href);
                    event.preventDefault();
                }
            }, false);
        }*/
    }


    public JFXPanel getContent() {
        return handoutContent;
    }

    public void updateContent(){
        if(webView != null) {
            Platform.setImplicitExit(false);
            Platform.runLater(() -> {
                webView.getEngine().load(urlString);
            });
        }
    }
}
