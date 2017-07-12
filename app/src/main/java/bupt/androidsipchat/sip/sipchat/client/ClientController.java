package bupt.androidsipchat.sip.sipchat.client;


import android.javax.sip.ClientTransaction;
import android.javax.sip.DialogTerminatedEvent;
import android.javax.sip.IOExceptionEvent;
import android.javax.sip.RequestEvent;
import android.javax.sip.ResponseEvent;
import android.javax.sip.ServerTransaction;
import android.javax.sip.TimeoutEvent;
import android.javax.sip.TransactionTerminatedEvent;
import android.javax.sip.message.Request;
import android.javax.sip.message.Response;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

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
public class ClientController implements SipProcessor, ChatClientService {

    public static final String INVITE_TO_CHAT = "INVITE_TO_CHAT";

    private SipUserAgent userAgent = null;
    private SipRequestBuilder requestBuilder = null;
    private SipResponseBuilder responseBuilder = null;
    private SipContactAOR serverAOR = null;

    private ConcurrentHashMap<Long, ClientTransaction> clientTransactionMap = null;
    private ConcurrentHashMap<Long, ServerTransaction> serverTransactionMap = null;

    public interface ResponseGet {
        void onRegisteResponseGet(boolean status);

        void onMessageResponseGet(boolean status);
    }

    private ResponseGet responseListener;

    public void setResponseListener(ResponseGet responseListener) {
        this.responseListener = responseListener;
    }


    public ClientController(Configuration sipConfig,
                            SipContactAOR selfAOR,
                            SipContactAOR serverAOR,
                            String transport) throws InitFailureException {
        try {
            userAgent = new SipUserAgent(sipConfig, selfAOR, transport) {

                @Override
                public void processRequest(RequestEvent requestEvent) {
                    String method = requestEvent.getRequest().getMethod();

                    switch (method) {
                        case Request.MESSAGE: {
                            ClientController.this.processMessage(requestEvent);
                            return;
                        }

                        case Request.NOTIFY: {
                            ClientController.this.processNotifier(requestEvent);
                            return;
                        }
                    }
                }

                @Override
                public void processResponse(ResponseEvent responseEvent) {
                    int statusCode = responseEvent.getResponse().getStatusCode();

                    if (200 <= statusCode && statusCode < 300) {
                        // todo handle successful case
                    } else {
                        // todo handle failed case
                    }
                }

                @Override
                public void processTimeout(TimeoutEvent timeoutEvent) {

                }

                @Override
                public void processIOException(IOExceptionEvent exceptionEvent) {

                }

                @Override
                public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {

                }

                @Override
                public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {

                }
            };

            this.responseBuilder = userAgent.createResponseBuilder();
            this.requestBuilder = userAgent.createRequestBuilder();
            this.serverAOR = serverAOR;
        } catch (Exception ex) {
            throw new InitFailureException("failed to init sipUserAgent", ex);
        }
    }

    @Override
    public void processInvite(RequestEvent requestEvent) {

    }

    @Override
    public void processBye(RequestEvent requestEvent) {
    }

    @Override
    public void processMessage(RequestEvent requestEvent) {
        ServerTransaction transaction = requestEvent.getServerTransaction();
        try {
            // todo something like updating ui components
            transaction.sendResponse(responseBuilder.create(transaction.getRequest(), Response.OK));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return;
    }

    @Override
    public void processRegister(RequestEvent requestEvent) {
    }

    @Override
    public void processPublish(RequestEvent requestEvent) {
    }

    @Override
    public void processNotifier(RequestEvent requestEvent) {
        ServerTransaction transaction = requestEvent.getServerTransaction();
        try {
            // todo something like updating ui components
            transaction.sendResponse(responseBuilder.create(transaction.getRequest(), Response.OK));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void processSubscribe(RequestEvent requestEvent) {
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        ClientTransaction clientTransaction = responseEvent.getClientTransaction();
        Response response = responseEvent.getResponse();

        ClientTransaction sendTransaction = clientTransactionMap.get(SipResponseBuilder.getResponseCSeq(response));

        Request request = sendTransaction.getRequest();

        switch (request.getMethod()) {
            case Request.REGISTER: {
                boolean stuatus = false;
                if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                    stuatus = true;
                }
                responseListener.onRegisteResponseGet(stuatus);
                break;
            }
            case Request.INVITE: {
                break;
            }
            case Request.MESSAGE: {
                boolean status = false;
                if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                    status = true;
                }
                responseListener.onMessageResponseGet(status);

            }
        }


        // todo
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        // todo


    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {
        // todo

    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        // todo
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        // todo
    }

    @Override
    public void login(String userName, String password) {

        try {
            Request request = requestBuilder.createRegister(serverAOR, 3600);
            ClientTransaction transaction = userAgent.sendRequestByTransaction(request);

            clientTransactionMap.put(SipRequestBuilder.getRequestCSeq(request), transaction);

        } catch (Exception e) {
            Log.e("Login", "Exception", e);
        }
    }

    @Override
    public void inviteToChat(String inviteeAOR) {
        try {
            // send invite to server to ask for the address of the invitee's address
            Request request = requestBuilder.createInvite(serverAOR, INVITE_TO_CHAT, inviteeAOR);
            ClientTransaction transaction = userAgent.sendRequestByTransaction(request);
            clientTransactionMap.put(SipRequestBuilder.getRequestCSeq(request), transaction);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String contactURI, String content) {
        try {
            Request request = requestBuilder.createMessage(new SipContactAOR(contactURI), content);
            ClientTransaction transaction = userAgent.sendRequestByTransaction(request);
            clientTransactionMap.put(SipRequestBuilder.getRequestCSeq(request), transaction);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void createChannel(String channelId) {
        try {
            Request request = requestBuilder.createSubscribe(serverAOR, channelId);
            ClientTransaction transaction = userAgent.sendRequestByTransaction(request);
            clientTransactionMap.put(SipRequestBuilder.getRequestCSeq(request), transaction);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void subscribeChannel(String channelId) {
        createChannel(channelId);
    }

    @Override
    public void publishToChannel(String channelId, String statusInfo) {
        try {
            Request request = requestBuilder.createPublish(serverAOR, channelId, statusInfo);
            ClientTransaction transaction = userAgent.sendRequestByTransaction(request);
            clientTransactionMap.put(SipRequestBuilder.getRequestCSeq(request), transaction);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
