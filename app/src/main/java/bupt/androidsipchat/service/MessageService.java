package bupt.androidsipchat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import bupt.androidsipchat.datestruct.MessageStruct;

/**
 * Created by sheju on 2017/7/8.
 */

public class MessageService extends Service {

    DataBinder dataBinder = new DataBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return dataBinder;
    }

    public class DataBinder extends Binder {
        public MessageService getService() {

            return MessageService.this;
        }
    }


    public interface MessageNotify {
        void onNewMessageArrive(MessageStruct message);

    }


}
