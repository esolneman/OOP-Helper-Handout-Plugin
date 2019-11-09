package eventHandling;

import com.intellij.notification.NotificationType;

import java.io.File;
import java.util.ArrayList;
//https://stackoverflow.com/questions/18279302/how-do-i-perform-a-java-callback-between-classes
//https://www.geeksforgeeks.org/asynchronous-synchronous-callbacks-java/

public interface OnGitEventListener {
    void onCloningRepositoryEvent(String notificationMessage, NotificationType messageType);
    void onUpdatingRepositoryEvent(ArrayList<String> commitMessages);
    //TODO implement Event
    void onNotUpdatingRepositoryEvent(String notificationMessage, NotificationType messageType);

}
