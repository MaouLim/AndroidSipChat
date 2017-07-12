package bupt.androidsipchat.sip.networks.sip;


import android.javax.sip.DialogTerminatedEvent;
import android.javax.sip.IOExceptionEvent;
import android.javax.sip.RequestEvent;
import android.javax.sip.ResponseEvent;
import android.javax.sip.TimeoutEvent;
import android.javax.sip.TransactionTerminatedEvent;

/*
 * Created by Maou Lim on 2017/7/10.
 */
public interface SipProcessor {

    void processInvite(RequestEvent requestEvent);
    void processBye(RequestEvent requestEvent);
    void processMessage(RequestEvent requestEvent);
    void processRegister(RequestEvent requestEvent);
    void processPublish(RequestEvent requestEvent);
    void processNotifier(RequestEvent requestEvent);
    void processSubscribe(RequestEvent requestEvent);
    void processACK(RequestEvent requestEvent);

    void processResponse(ResponseEvent responseEvent);

    void processTimeout(TimeoutEvent timeoutEvent);
    void processIOException(IOExceptionEvent exceptionEvent);
    void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent);
    void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent);
}
