package Listener;

import java.io.File;

public interface OnEventListener {
    void onCloningRepositoryEvent(File repoFile);
}
