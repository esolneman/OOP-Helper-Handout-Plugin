package webView;

import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.apache.commons.lang.WordUtils;

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
        if(heading.contains(" ")){
            //https://stackoverflow.com/a/1892778
            heading = WordUtils.capitalize(heading);
            //https://stackoverflow.com/a/15633284
            heading = heading.replaceAll("\\s+","");
        }
        String newLocation = urlString + "#" + heading;
        System.out.println(newLocation);
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView.getEngine().load(newLocation);
        });
    }
}
