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
import bupt.androidsipchat.datestruct.Channel;
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

    public List<Channel> channelMessageList = new ArrayList<>();

    private String chartRoomName = "大型同性交友聊天室";

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

        LogOut();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return dataBinder;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("OnTaskRemoved", "LogOut");

        LogOut();

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
            public void onMessageRequestGet(String from, String messages, String to) {


                if (to.equals("server")) {
                    //to = chartRoomName;

                    MessageStruct messageStruct = new MessageStruct(from, messages, id);
                    messageStruct.isChatRoomMessage = true;

                    boolean isFind = false;

                    int i = 0;
                    for (; i < dialogMessageList.size(); i++) {
                        if (dialogMessageList.get(i).toName.equals(to)) {
                            break;
                        }
                    }
                    if (i < dialogMessageList.size()) {
                        isFind = true;
                        Log.e("Message", "Find the dialog");
                        DialogMessage dialogMessage = dialogMessageList.get(i);

                        messageStruct.setSpecialId(dialogMessage.id);
                        dialogMessage.messageStructs.add(messageStruct);
                        dialogMessage.idSequence = id;

                    } else {
                        Log.e("Message", "Create the dialog");
                        DialogMessage dialogMessage = new DialogMessage(userName, from);
                        dialogMessage.isChatRoom = true;
                        messageStruct.setSpecialId(dialogMessage.id);
                        dialogMessage.messageStructs.add(messageStruct);
                        dialogMessage.idSequence = id;
                        dialogMessageList.add(dialogMessage);
                    }

                    if (messageNotify != null) {
                        messageNotify.onNewMessageArrive(messageStruct, isFind);
                    }
                    if (chatNotify != null) {
                        chatNotify.onNewMessageArrive(messageStruct, isFind);
                    }

                    id++;


                } else {

                    MessageStruct messageStruct = new MessageStruct(from, messages, id);

                    boolean isFind = false;


                    int i = 0;
                    for (; i < dialogMessageList.size(); i++) {
                        if (dialogMessageList.get(i).toName.equals(from)) {
                            break;
                        }
                    }
                    if (i < dialogMessageList.size()) {
                        isFind = true;
                        Log.e("Message", "Find the dialog");
                        DialogMessage dialogMessage = dialogMessageList.get(i);
                        messageStruct.setSpecialId(dialogMessage.id);
                        dialogMessage.messageStructs.add(messageStruct);
                        dialogMessage.idSequence = id;

                    } else {
                        Log.e("Message", "Create the dialog");
                        DialogMessage dialogMessage = new DialogMessage(userName, from);
                        messageStruct.setSpecialId(dialogMessage.id);
                        dialogMessage.messageStructs.add(messageStruct);
                        dialogMessage.idSequence = id;
                        dialogMessageList.add(dialogMessage);
                    }

                    if (messageNotify != null) {
                        messageNotify.onNewMessageArrive(messageStruct, isFind);
                    }
                    if (chatNotify != null) {
                        chatNotify.onNewMessageArrive(messageStruct, isFind);
                    }

                    id++;
                }
            }

            @Override
            public void onNotifyRequestGet(String from, String title, String content, String channelName) {
                MessageStruct messageStruct = new MessageStruct(title, content, id);
                id++;
                messageStruct.setSubTitle(from);
                int specialId = -1;
                for (Channel c : channelMessageList) {
                    if (c.getTitle().equals(channelName)) {
                        specialId = c.getId();
                        break;
                    }
                }

                messageStruct.setSpecialId(specialId);

                if (channelMessageNotify != null) {
                    channelMessageNotify.onChannelMessageArrive(messageStruct);
                }
                //channelNotify.onChannelListArrive(messageStruct);
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
                channelPubResult.onPublishResponse(status);
            }

            @Override
            public void onSubscribeResponseGet(boolean status) {
                channelSubResult.onSubscribeResponse(status);
            }
        });

    }

    public void login(final UserInformation user) {

        final String add = user.getUserName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("Service", "Login");
                clientController.login(add, user.password);
            }
        }).start();
    }

    public void sendMessage(String to, String content, int dialogId) {
        String trueTo = to;
        if (to.equals(chartRoomName)) {
            to = "server";
        }

        final String sipAdd = "sip:" + to + "@" + clientController.domain;
        final String m = content;

        for (DialogMessage dm : dialogMessageList) {
            if (dm.id == dialogId) {
                dm.messageStructs.add(new MessageStruct(trueTo, content, id));
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

        void onNewMessageArrive(MessageStruct message, boolean isFind);

    }

    MessageNotify messageNotify;

    public interface ChatNotify {
        void onNewMessageArrive(MessageStruct message, boolean isFind);
    }

    public void setChatNotify(ChatNotify chatNotify) {
        this.chatNotify = chatNotify;
    }

    ChatNotify chatNotify;

    public void setMessageNotify(MessageNotify messageNotify) {
        this.messageNotify = messageNotify;
    }


    public interface ChannelNotify {
        void onChannelListArrive(MessageStruct message);
    }

    ChannelNotify channelNotify;

    public void setChannelNotify(ChannelNotify channelNotify) {
        this.channelNotify = channelNotify;
    }

    public interface ChannelMessageNotify {
        void onChannelMessageArrive(MessageStruct messageStruct);
    }

    ChannelMessageNotify channelMessageNotify;

    public void setChannelMessageNotify(ChannelMessageNotify channelMessageNotify) {
        this.channelMessageNotify = channelMessageNotify;
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

    public interface ChannelPubResult {
        void onPublishResponse(boolean status);
    }

    ChannelPubResult channelPubResult;

    public void setChannelPubResult(ChannelPubResult channelPubResult) {
        this.channelPubResult = channelPubResult;
    }


    public interface ChannelSubResult {

        void onSubscribeResponse(boolean status);
    }

    ChannelSubResult channelSubResult;

    public void setChannelSubResult(ChannelSubResult channelSubResult) {
        this.channelSubResult = channelSubResult;
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
            if (j > 0) {
                j = j - 1;
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

    public List<MessageStruct> getChannelList() {
        List<MessageStruct> channelList = new ArrayList<>();

        for (Channel c : channelMessageList) {
            MessageStruct m = new MessageStruct(c.getTitle(), c.getContent(), 0);
            m.setSpecialId(c.getId());
            m.setSubTitle(c.getSubTitle());
            channelList.add(m);
        }

        return channelList;

    }

    public List<MessageStruct> getChannelMessages(int id) {
        List<MessageStruct> messageStructs = new ArrayList<>();

        for (int i = 0; i < channelMessageList.size(); i++) {
            if (channelMessageList.get(i).getId() == id) {
                messageStructs.addAll(channelMessageList.get(i).getChannelMessage());
                break;
            }
        }

        return messageStructs;

    }

    public void addNewChannelMessage(MessageStruct m) {
        for (Channel c : channelMessageList) {
            if (c.getId() == m.getSpecialId()) {
                c.getChannelMessage().add(new MessageStruct(m));
                break;
            }
        }
    }

    public void addNewChannel(Channel m) {
        channelMessageList.add(m);
    }

    public void addNewDialog(DialogMessage dialogMessage) {
        this.dialogMessageList.add(dialogMessage);
    }

    public void LogOut() {

        clientController.logOut(user.getUserName());


        //clientController.close();
    }

    public void closeController() {
        if (clientController != null) {
            clientController.close();
        }
    }

}
