package provider;

import eventHandling.OnGitEventListener;

public interface HandoutContentDataProviderInterface {
    static HandoutContentDataProvider getInstance() {
        return null;
    }

    void updateHandoutData();

    void addListener(OnGitEventListener listener);

    //public void cloneRepository();
    //public void updateRepossitory();
    //void getLocalRepository();
}
