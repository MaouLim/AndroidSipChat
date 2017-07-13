package bupt.androidsipchat.datestruct;

import bupt.androidsipchat.sip.networks.sip.SipAOR;
import bupt.androidsipchat.sip.networks.sip.SipContactAOR;

/**
 * Created by sheju on 2017/7/12.
 */

public class UserInformation {

    SipContactAOR sipContactAOR = null;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    String userName;
    String domain = "dd.dev.com";
    public String password = "";

    public UserInformation(String userName) {
        this.userName = userName;


    }

    public String getSipAddress() {
        return "sip:" + userName + "@" + domain;
    }

    public SipAOR getSipAOR() {
        return new SipAOR(userName, domain);
    }

    public SipContactAOR getContactAOR(String ip) {
        if (sipContactAOR == null) {
            sipContactAOR = new SipContactAOR(userName, ip, 12345);
            sipContactAOR.attachTo(new SipAOR(userName, domain));
        }
        return sipContactAOR;
    }

}
