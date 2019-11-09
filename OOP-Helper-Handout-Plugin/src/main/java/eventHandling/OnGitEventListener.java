package eventHandling;

import java.io.File;
import java.util.ArrayList;
//https://stackoverflow.com/questions/18279302/how-do-i-perform-a-java-callback-between-classes
//https://www.geeksforgeeks.org/asynchronous-synchronous-callbacks-java/

public interface OnGitEventListener {
    void onCloningRepositoryEvent();
    void onUpdatingRepositoryEvent(ArrayList<String> commitMessages);
}
