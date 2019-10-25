package webView;

import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.apache.commons.lang.WordUtils;
import org.w3c.dom.*;

import javax.swing.text.html.HTML;

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
    public void goToLocation(String heading){
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
        //Node mark =;
        String finalHeading = heading;
        Platform.runLater(() -> {

            Element ele = (Element) webView.getEngine().getDocument().getElementById(finalHeading).getParentNode();
            System.out.println("ele: " + ele.getTagName());
            Element mark = webView.getEngine().getDocument().createElement("b");
            System.out.println("mrk: " + mark.getTagName());
            Element parentElement = (Element) ele.getParentNode();
            System.out.println("ele getParentNode : " + parentElement.getNodeName());
            //ele.appendChild(mark);
            parentElement.insertBefore(mark, ele);
            mark.appendChild(ele);
            System.out.println("ele getParentNode -new: " + ele.getParentNode());



            //webView.getEngine().getDocument().getElementById(finalHeading)
            webView.getEngine().load(newLocation);
            webView.getEngine().reload();
            //Attr mark = Attr;
        });
    }
}
