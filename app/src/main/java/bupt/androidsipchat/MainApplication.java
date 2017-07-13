package bupt.androidsipchat;

import android.app.Application;
import android.content.Intent;

import bupt.androidsipchat.service.MessageService;

/**
 * Created by sheju on 2017/7/13.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MessageService.class));

    }

    @Override
    public void onTerminate() {
        super.onTerminate();


    }
}
