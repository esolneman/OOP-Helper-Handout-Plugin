package eventHandling;

import com.google.common.eventbus.Subscribe;

import java.io.File;

public interface DataProviderListener {

    @Subscribe
    void DataProviderListener(File repoFile);
}
