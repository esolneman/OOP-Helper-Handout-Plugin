package webView;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindow;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.apache.commons.lang.WordUtils;
import toolWindow.HandoutContentScreen;

public class WebViewController {
    private WebView webView;
    private WebViewLinkListener webViewLinkListener;
    private String urlString;

    public WebViewController(){
    }

    public WebView createWebView(String urlString){
        this.urlString = urlString;
        //Platform.setImplicitExit(false);
        //Platform.runLater(() -> {
            webView = new WebView();
            WebViewLinkListener webViewLinkListener = new WebViewLinkListener(webView, urlString);
            return webView;
        //});
    }

    //https://stackoverflow.com/questions/49070734/javafx-webview-link-to-anchor-in-document-doesnt-work-using-loadcontent
    public void goToLocation(String heading, ToolWindow handoutToolWindow, HandoutContentScreen handoutContentScreen){
        System.out.println("heading: " + heading);
        //TODO: check or if (class ref)
        if(heading.contains(" ")){
            //https://stackoverflow.com/a/1892778
            heading = WordUtils.capitalize(heading);
            //https://stackoverflow.com/a/15633284
            heading = heading.replaceAll("\\s+","");
        }
        String newLocation = urlString + "#" + heading;
        System.out.println(newLocation);
        Platform.setImplicitExit(false);
        String finalHeading = heading;
        ApplicationManager.getApplication().invokeLater(() -> {
            System.out.println("ToolWindow isVisible: "+ handoutToolWindow.isVisible());

            if(handoutToolWindow.isVisible()) {
                //open HandoutToolWindow and select the correct tab
                handoutToolWindow.getContentManager().setSelectedContent(handoutToolWindow.getContentManager().getContent(handoutContentScreen.getContent()));
            }else{
                //TODO open small part of tool window
                handoutToolWindow.activate(() -> {
                    handoutToolWindow.getContentManager().setSelectedContent(handoutToolWindow.getContentManager().getContent(handoutContentScreen.getContent()));
                    //ToolWindowViewModeAction toolWindowViewModeAction
                    //ToolWindowViewModeAction.ViewMode.DockUnpinned;
                    //handoutToolWindow.;
                });
            }
        });
        Platform.runLater(() -> {
            /*
            //https://stackoverflow.com/questions/52960101/how-to-edit-html-page-in-a-webview-from-javafx-without-reloading-the-page

            final Document document;
            Element ele = webView.getEngine().getDocument().getElementById(finalHeading);
            System.out.println("ele: " + ele.getTagName());
            Element mark = webView.getEngine().getDocument().createElement("mark");
            System.out.println("mrk: " + mark.getTagName());
            Element parentElement = (Element) ele.getParentNode();
            System.out.println("ele getParentNode : " + parentElement.getNodeName());
            parentElement.insertBefore(mark, ele);
            mark.appendChild(ele);
            System.out.println("ele getParentNode -new: " + ele.getParentNode());

             DOMSource domSource = new DOMSource(webView.getEngine().getDocument());

             StreamResult result = new StreamResult(urlString);
             TransformerFactory tf = TransformerFactory.newInstance();
             Transformer transformer = null;

             try {
                 transformer = tf.newTransformer();
                 transformer.setOutputProperty(OutputKeys.METHOD, "html");
                 transformer.transform(domSource, result);
             } catch (TransformerConfigurationException e) {
                 e.printStackTrace();
             } catch (TransformerException e) {
                 e.printStackTrace();
             }



            try {
                //https://stackoverflow.com/questions/29087077/is-it-possible-to-convert-html-into-xhtml-with-jsoup-1-8-1

                document = Jsoup.parse(LocalStorageDataProvider.getHandoutFileDirectory(), "UTF-8");
                document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
                File outputFile = new File(LocalStorageDataProvider.getHandoutStringDirectory());
                FileUtils.writeStringToFile(outputFile, document.html(), "UTF-8");

            } catch (IOException e) {
                e.printStackTrace();
            }*/

            if (finalHeading.contains("/")){
                webView.getEngine().load(newLocation);

            }else {
                webView.getEngine().load(newLocation);
            }

        });
    }
}
