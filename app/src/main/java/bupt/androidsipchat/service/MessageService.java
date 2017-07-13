package bupt.androidsipchat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.adapter.ChatRecycleViewAdapter;
import bupt.androidsipchat.datestruct.DialogMessage;
import bupt.androidsipchat.datestruct.MessageStruct;
import bupt.androidsipchat.datestruct.UserInformation;
import bupt.androidsipchat.sip.networks.sip.SipAgent;
import bupt.androidsipchat.sip.networks.sip.SipContactAOR;
import bupt.androidsipchat.sip.networks.sip.exceptions.InitFailureException;
import bupt.androidsipchat.sip.sipchat.client.ClientController;
import bupt.androidsipchat.sip.util.ClientSipConfigurationHelper;
import bupt.androidsipchat.sip.util.IPAddressHelper;

/**
 * Created by sheju on 2017/7/8.
 *
 */

public class MessageService extends Service {


    public List<DialogMessage> dialogMessageList = new ArrayList<>();

    DataBinder dataBinder = new DataBinder();

    ClientController clientController;
    public UserInformation user;

    public int getId() {
        return id;
    }

    int id = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

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

    public void initSipController(final String userName, String password) {
        user = new UserInformation(userName);

        ClientSipConfigurationHelper clientSipConfigurationHelper = new ClientSipConfigurationHelper(this);
        SipContactAOR serverAOR = new SipContactAOR("server", "10.209.13.116", 5060);

        try {
            clientController = new ClientController(clientSipConfigurationHelper.createConfiguration(),
                    user.getContactAOR(IPAddressHelper.getIPAddress(this)), serverAOR, SipAgent.UDP);
        } catch (InitFailureException e) {
            Log.e("ClientController", " ", e);
        }

        clientController.setRequestListener(new ClientController.RequestGet() {
            @Override
            public void onMessageRequestGet(String from, String messages) {
                MessageStruct messageStruct = new MessageStruct(from, messages, id);

                if (messageNotify != null) {
                    messageNotify.onNewMessageArrive(messageStruct);
                }
                if (chatNotify != null) {
                    chatNotify.onNewMessageArrive(messageStruct);
                }

                int i = 0;
                for (; i < dialogMessageList.size(); i++) {
                    if (dialogMessageList.get(i).toName.equals(from)) {
                        break;
                    }
                }
                if (i < dialogMessageList.size()) {
                    DialogMessage dialogMessage = dialogMessageList.get(i);
                    dialogMessage.messageStructs.add(new MessageStruct(from, messages, id));
                    dialogMessage.idSequence = id;

                } else {
                    DialogMessage dialogMessage = new DialogMessage(userName, from);
                    dialogMessage.messageStructs.add(new MessageStruct(from, messages, id));
                    dialogMessage.idSequence = id;
                    dialogMessageList.add(dialogMessage);
                }


                id++;
            }

            @Override
            public void onNotifyRequestGet(String from, String title, String content) {
                MessageStruct messageStruct = new MessageStruct(title, content, id);
                id++;
                messageStruct.setSubTitle(from);
                channelNotify.onChannelMessageArrive(messageStruct);
            }

            @Override
            public void onByeRequestGet(String from) {
            }
        });

        clientController.setResponseListener(new ClientController.ResponseGet() {
            @Override
            public void onRegisteResponseGet(boolean status) {
                registLoginResult.onRegistLogin(status);
            }

            @Override
            public void onMessageResponseGet(boolean status) {
                messageResult.onMessage(status);
            }

            @Override
            public void onPublishResponseGet(boolean status) {
                channelResult.onPublishResponse(status);
            }

            @Override
            public void onSubscribeResponseGet(boolean status) {
                channelResult.onSubscribeResponse(status);
            }
        });

    }

    public void login(UserInformation user) {

        final String add = user.getUserName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("Service", "Login");
                clientController.login(add, " ");
            }
        }).start();
    }

    public void sendMessage(String to, String content, int dialogId) {
        final String sipAdd = "sip:" + to + "@" + clientController.domain;
        final String m = content;

        for (DialogMessage dm : dialogMessageList) {
            if (dm.id == dialogId) {
                dm.messageStructs.add(new MessageStruct(to, content, id));
                break;
            }
        }
        id++;
        new Thread(new Runnable() {
            @Override
            public void run() {

                clientController.sendMessage(sipAdd, m);


            }
        }).start();
    }

    public void subscribeChannel(String name) {
        final String n = name;
        new Thread(new Runnable() {
            @Override
            public void run() {
                clientController.subscribeChannel(n);
            }
        }).start();
    }

    public void publishToChannel(final String title, String content, String channel) {
        final String t = title;
        final String c = content;
        final String id = channel;

        new Thread(new Runnable() {
            @Override
            public void run() {
                clientController.publishToChannel(id, title + "/" + c);
            }
        }).start();
    }



    public interface MessageNotify {

        void onNewMessageArrive(MessageStruct message);

    }

    MessageNotify messageNotify;

    public interface ChatNotify {
        void onNewMessageArrive(MessageStruct message);
    }

    public void setChatNotify(ChatNotify chatNotify) {
        this.chatNotify = chatNotify;
    }

    ChatNotify chatNotify;

    public void setMessageNotify(MessageNotify messageNotify) {
        this.messageNotify = messageNotify;
    }


    public interface ChannelNotify {
        void onChannelMessageArrive(MessageStruct message);
    }

    ChannelNotify channelNotify;

    public void setChannelNotify(ChannelNotify channelNotify) {
        this.channelNotify = channelNotify;
    }


    public interface RegistLoginResult {
        void onRegistLogin(boolean status);
    }

    RegistLoginResult registLoginResult;

    public void setRegistLoginResult(RegistLoginResult registLoginResult) {
        this.registLoginResult = registLoginResult;
    }


    public interface MessageResult {
        void onMessage(boolean status);
    }

    MessageResult messageResult;

    public void setMessageResult(MessageResult result) {
        this.messageResult = result;
    }


    public interface ChannelResult {
        void onPublishResponse(boolean status);

        void onSubscribeResponse(boolean status);
    }

    ChannelResult channelResult;

    public void setChannelResult(ChannelResult channelResult) {
        this.channelResult = channelResult;
    }

    public List<MessageStruct> getDialogs() {
        List<MessageStruct> messageStructs = new ArrayList<>();

        int min = -1;
        for (int i = 0; i < dialogMessageList.size(); i++) {

            int j = 0;
            for (; j < dialogMessageList.size(); j++) {

                if (dialogMessageList.get(i).idSequence > min) {
                    min = dialogMessageList.get(i).idSequence;
                }
            }
            MessageStruct last = dialogMessageList.get(j).messageStructs.get(dialogMessageList.get(j).messageStructs.size() - 1);
            MessageStruct copy = new MessageStruct(last);
            copy.setSpecialId(dialogMessageList.get(j).id);
            messageStructs.add(0, copy);
        }
        return messageStructs;
    }

    public List<MessageStruct> getChatMessageHistory(int id) {
        List<MessageStruct> messageStructs = new ArrayList<>();

        for (int i = 0; i < dialogMessageList.size(); i++) {
            if (dialogMessageList.get(i).id == id) {
                messageStructs.addAll(dialogMessageList.get(i).messageStructs);
                break;
            }
        }

        for (MessageStruct ms : messageStructs) {
            if (ms.getTitle().equals(user.getUserName())) {
                ms.setViewType(ChatRecycleViewAdapter.SELFTYPE);
            } else {
                ms.setViewType(ChatRecycleViewAdapter.OTHERTYPE);
            }
        }


        return messageStructs;

    }

}
