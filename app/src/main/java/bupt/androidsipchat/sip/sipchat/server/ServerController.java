package bupt.androidsipchat.sip.sipchat.server;


import android.javax.sip.ClientTransaction;
import android.javax.sip.DialogTerminatedEvent;
import android.javax.sip.IOExceptionEvent;
import android.javax.sip.RequestEvent;
import android.javax.sip.ResponseEvent;
import android.javax.sip.ServerTransaction;
import android.javax.sip.TimeoutEvent;
import android.javax.sip.TransactionTerminatedEvent;
import android.javax.sip.header.ContactHeader;
import android.javax.sip.header.EventHeader;
import android.javax.sip.header.FromHeader;
import android.javax.sip.header.ToHeader;
import android.javax.sip.message.Request;
import android.javax.sip.message.Response;

import java.util.concurrent.ConcurrentHashMap;

import bupt.androidsipchat.sip.networks.sip.SipAOR;
import bupt.androidsipchat.sip.networks.sip.SipContactAOR;
import bupt.androidsipchat.sip.networks.sip.SipProcessor;
import bupt.androidsipchat.sip.networks.sip.SipRequestBuilder;
import bupt.androidsipchat.sip.networks.sip.SipResponseBuilder;
import bupt.androidsipchat.sip.networks.sip.SipUserAgent;
import bupt.androidsipchat.sip.networks.sip.exceptions.InitFailureException;
import bupt.androidsipchat.sip.util.Configuration;

/*
 * Created by Maou Lim on 2017/7/10.
 */
public class ServerController implements SipProcessor {

    public static final String SERVER_NAME = "server_name";
    public static final String SERVER_DOMAIN = "server_domain";
    public static final String IP_ADDRESS = "javax.sip.IP_ADDRESS";
    public static final String SERVER_PORT = "server_port";
    public static final String SERVER_TRANSPORT = "server_transport";

    private SipUserAgent agent = null;

    private SipRequestBuilder requestBuilder = null;
    private SipResponseBuilder responseBuilder = null;

    private ConcurrentHashMap<Long, ClientTransaction> clientTransactionMap = null;
    private ConcurrentHashMap<Long, ServerTransaction> serverTransactionMap = null;

    private ConcurrentHashMap<String, SipContactAOR> contactMap = null;
    private ConcurrentHashMap<String, Event> events = null;

    public ServerController(String sipConfigURL, String serverConfigURL) throws InitFailureException {

        Configuration sipConfig = new Configuration(sipConfigURL);
        Configuration serverConfig = new Configuration(serverConfigURL);

        SipAOR sipAOR = new SipAOR(
                (String) serverConfig.get(SERVER_NAME),
                (String) serverConfig.get(SERVER_DOMAIN)
        );

        SipContactAOR contactAOR = new SipContactAOR(
                (String) serverConfig.get(SERVER_NAME),
                (String) sipConfig.get(IP_ADDRESS),
                (Integer) serverConfig.get(SERVER_PORT),
                sipAOR
        );

        try {
            this.agent = new SipUserAgent(sipConfig, contactAOR, (String) serverConfig.get(SERVER_TRANSPORT)) {

                @Override
                public void processRequest(RequestEvent requestEvent) {
                    String method = requestEvent.getRequest().getMethod();

                    switch (method) {
                        case Request.INVITE: {
                            ServerController.this.processInvite(requestEvent);
                            return;
                        }

                        case Request.BYE: {
                            ServerController.this.processBye(requestEvent);
                            return;
                        }

                        case Request.MESSAGE: {
                            ServerController.this.processMessage(requestEvent);
                            return;
                        }

                        case Request.REGISTER: {
                            ServerController.this.processRegister(requestEvent);
                            return;
                        }

                        case Request.PUBLISH: {
                            ServerController.this.processPublish(requestEvent);
                            return;
                        }

                        case Request.SUBSCRIBE: {
                            ServerController.this.processSubscribe(requestEvent);
                            return;
                        }

                        case Request.NOTIFY: {
                            ServerController.this.processNotifier(requestEvent);
                        }
                    }
                }

                @Override
                public void processResponse(ResponseEvent responseEvent) {
                    ServerController.this.processResponse(responseEvent);
                }

                @Override
                public void processTimeout(TimeoutEvent timeoutEvent) {
                    ServerController.this.processTimeout(timeoutEvent);
                }

                @Override
                public void processIOException(IOExceptionEvent exceptionEvent) {
                    ServerController.this.processIOException(exceptionEvent);
                }

                @Override
                public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
                    ServerController.this.processTransactionTerminated(transactionTerminatedEvent);
                }

                @Override
                public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
                    System.err.println("DialogTerminated");
                }
            };

            this.requestBuilder = this.agent.createRequestBuilder();
            this.responseBuilder = this.agent.createResponseBuilder();

            this.clientTransactionMap = new ConcurrentHashMap<>();
            this.serverTransactionMap = new ConcurrentHashMap<>();

            this.contactMap = new ConcurrentHashMap<>();
            this.events = new ConcurrentHashMap<>();
        } catch (Exception ex) {
            throw new InitFailureException("SipAgent init failure.", ex);
        }
    }

    @Override
    public void processInvite(RequestEvent requestEvent) {
        ServerTransaction transaction = requestEvent.getServerTransaction();
        Request request = transaction.getRequest();

        String toURI =
                ((ToHeader) request.getHeader(ToHeader.NAME)).getAddress().getURI().toString();

        SipContactAOR contactAOR = contactMap.get(toURI);

        /* no such user, reply not-found */
        if (null == contactAOR) {

            try {
                transaction.sendResponse(responseBuilder.create(request, Response.NOT_FOUND));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }

        /* else send try to the invitor */
        else {
            try {

                transaction.sendResponse(responseBuilder.create(request, Response.TRYING));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        /* forward it to the invitee */
        try {
            request.setRequestURI(contactAOR.getSipURI());
            agent.sendRequestByTransaction(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void processBye(RequestEvent requestEvent) {
        ServerTransaction transaction = requestEvent.getServerTransaction();
        Request request = transaction.getRequest();

        String toURI =
                ((ToHeader) request.getHeader(ToHeader.NAME)).getAddress().getURI().toString();

        SipContactAOR contactAOR = contactMap.get(toURI);

        /* no such user */
        if (null == contactAOR) {

            try {
                transaction.sendResponse(responseBuilder.create(request, Response.TRYING));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        } else {
            try {
                transaction.sendResponse(responseBuilder.create(request, Response.OK));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            request.setRequestURI(contactAOR.getSipURI());
            agent.sendRequestByTransaction(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void processMessage(RequestEvent requestEvent) {
        ServerTransaction transaction = requestEvent.getServerTransaction();
        Request request = transaction.getRequest();
        String toURI =
                ((ToHeader) request.getHeader(ToHeader.NAME)).getAddress().getURI().toString();

        // todo record the content

        SipContactAOR contactAOR = contactMap.get(toURI);
        if (null != contactAOR) {
            ContactHeader contactHeader = (ContactHeader) request.getHeader(ContactHeader.NAME);
            contactHeader.setAddress(contactAOR.getSipAddress());

            try {
                agent.sendRequestByTransaction(request);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            transaction.sendResponse(responseBuilder.create(request, Response.OK));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void processRegister(RequestEvent requestEvent) {
        ServerTransaction transaction = requestEvent.getServerTransaction();
        Request request = transaction.getRequest();

        String contactURI =
                ((ContactHeader) request.getHeader(ContactHeader.NAME)).getAddress().getURI().toString();
        String toURI =
                ((ToHeader) request.getHeader(ToHeader.NAME)).getAddress().getURI().toString();

        SipContactAOR contactAOR = new SipContactAOR(contactURI);
        contactAOR.attachTo(new SipAOR(toURI));

        /* if the user name exists, reply ambiguous */
        if (contactMap.containsKey(toURI)) {
            try {
                transaction.sendResponse(responseBuilder.create(request, Response.AMBIGUOUS));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }

        /* put into the map */
        contactMap.put(toURI, contactAOR);

        /* reply ok */
        try {
            transaction.sendResponse(responseBuilder.create(request, Response.OK));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void processPublish(RequestEvent requestEvent) {
        ServerTransaction transaction = requestEvent.getServerTransaction();
        Request request = transaction.getRequest();

        /* the new status information of the event has been published */
        String content = (String) request.getContent();

        /* if the event exists, notify all the subscriber */
//        events.computeIfPresent(
//            ((EventHeader) request.getHeader(EventHeader.NAME)).getEventType(),
//            (eventName, eventMatched) -> {
//                List<SipAOR> subscribers = eventMatched.getSubscribers();
//
//                for (SipAOR each : subscribers) {
//                    SipContactAOR contactAOR = contactMap.get(each.toString());
//
//                    if (null == contactAOR) {
//                        continue;
//                    }
//
//                    try {
//                        Request notify = requestBuilder.createNotify(contactAOR, eventName, content);
//                        notify.setHeader(request.getHeader(FromHeader.NAME));
//                        agent.sendRequestByTransaction(notify);
//                    }
//                    catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//
//                return null;
//            }
//        );

        /* send ok to the publisher */
        try {
            transaction.sendResponse(responseBuilder.create(request, Response.OK));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void processNotifier(RequestEvent requestEvent) {
    }

    @Override
    public void processSubscribe(RequestEvent requestEvent) {
        ServerTransaction transaction = requestEvent.getServerTransaction();
        Request request = transaction.getRequest();

        EventHeader eventHeader = (EventHeader) request.getHeader(EventHeader.NAME);
        FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);

        String eventName = eventHeader.getEventType();
        events.putIfAbsent(eventName, new Event(eventName));

        Event event = events.get(eventName);
        event.subscribe(new SipAOR(fromHeader.getAddress().getURI().toString()));

        try {
            transaction.sendResponse(responseBuilder.create(request, Response.OK));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {

    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        System.err.println("timeout");
    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {
        System.err.println("IOException");
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        System.err.println("TransactionTerminated");
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {

    }
}
