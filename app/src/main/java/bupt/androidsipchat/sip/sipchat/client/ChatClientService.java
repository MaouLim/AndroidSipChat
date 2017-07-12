package bupt.androidsipchat.sip.sipchat.client;

/*
 * Created by Maou Lim on 2017/7/11.
 */
public interface ChatClientService {

    void login(String userName, String password);

    void inviteToChat(String inviteeAOR);

    void sendMessage(String contactURI, String content);

    void createChannel(String channelId);

    void subscribeChannel(String channelId);

    void publishToChannel(String channelId, String statusInfo);
}
