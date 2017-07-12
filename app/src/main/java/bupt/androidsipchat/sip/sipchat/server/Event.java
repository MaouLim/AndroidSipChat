package bupt.androidsipchat.sip.sipchat.server;


import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.sip.networks.sip.SipAOR;


/*
 * Created by Maou Lim on 2017/7/11.
 */
public class Event {

    private String eventName = null;
    private List<SipAOR> subscribers = null;

    public Event(String eventName) {
        this.eventName = eventName;
        this.subscribers = new ArrayList<>();
    }

    public void subscribe(SipAOR subscribe) {
        subscribers.add(subscribe);
    }

    public void cancelSubscribe(SipAOR subscribe) {
        subscribers.remove(subscribe);
    }

    public String getEventName() {
        return eventName;
    }

    public List<SipAOR> getSubscribers() {
        return subscribers;
    }
}
