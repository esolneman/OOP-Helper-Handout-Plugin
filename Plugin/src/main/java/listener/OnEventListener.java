package listener;

import java.io.File;
//https://stackoverflow.com/questions/18279302/how-do-i-perform-a-java-callback-between-classes
//https://www.geeksforgeeks.org/asynchronous-synchronous-callbacks-java/
public interface OnEventListener {
    void onCloningRepositoryEvent(File repoFile);
}
