package environment;

import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class HandoutPluginFXPanel extends JFXPanel {

        public void showContent() {
            System.out.println("in: showContent");
            Platform.setImplicitExit(false);
            Platform.runLater(() -> {
                System.out.println("in: showContent (RunLater-Part)");
                Label helloWord = new Label();
                helloWord.setText("Hello World");
                this.setScene(new Scene(helloWord, 50, 50));
            });
        }

        public void setWebView() {

        }

        public void sayName() {
            System.out.println("I am a MyJFXPanel");
        }

}
