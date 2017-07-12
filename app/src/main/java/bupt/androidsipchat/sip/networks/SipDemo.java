package bupt.androidsipchat.sip.networks;


import android.javax.sip.DialogTerminatedEvent;
import android.javax.sip.IOExceptionEvent;
import android.javax.sip.RequestEvent;
import android.javax.sip.ResponseEvent;
import android.javax.sip.TimeoutEvent;
import android.javax.sip.TransactionTerminatedEvent;
import android.javax.sip.message.Request;
import android.javax.sip.message.Response;

import java.net.InetAddress;
import java.util.Scanner;

import bupt.androidsipchat.sip.networks.sip.SipAOR;
import bupt.androidsipchat.sip.networks.sip.SipAgent;
import bupt.androidsipchat.sip.networks.sip.SipContactAOR;
import bupt.androidsipchat.sip.networks.sip.SipRequestBuilder;
import bupt.androidsipchat.sip.networks.sip.SipUserAgent;
import bupt.androidsipchat.sip.util.Configuration;

public class SipDemo {

    public static void main(String[] args)
            throws Exception {

        String name = args[0];
        int port = Integer.parseInt(args[1]);
        String domain = "dd.dev.com";

        Configuration configuration = new Configuration("res/sip-config.yml");

        SipAOR sipAOR = new SipAOR(name, domain);
        SipContactAOR contactAOR =
                new SipContactAOR(name, InetAddress.getLocalHost().getHostAddress(), port, sipAOR);

        System.out.println("local: " + contactAOR.getAddress() + ":" + contactAOR.getPort());

        SipUserAgent userAgent = new SipUserAgent(configuration, contactAOR, SipAgent.UDP) {

            @Override
            public void processRequest(RequestEvent requestEvent) {
                Request request = requestEvent.getRequest();
                System.err.println(request.toString());
            }

            @Override
            public void processResponse(ResponseEvent responseEvent) {
                Response response = responseEvent.getResponse();
                System.err.println(response.toString());
            }

            @Override
            public void processTimeout(TimeoutEvent timeoutEvent) {
                System.err.println("timeout");
            }

            @Override
            public void processIOException(IOExceptionEvent exceptionEvent) {
                System.err.println("exception");
            }

            @Override
            public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
                System.err.println("processTransactionTerminated");
            }

            @Override
            public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
                System.err.println("processDialogTerminated");
            }
        };

        SipRequestBuilder builder = userAgent.createRequestBuilder();

        Scanner scanner = new Scanner(System.in);

        String line = null;
        while (null != (line = scanner.nextLine())) {
            SipContactAOR toContact = new SipContactAOR(line);
            SipAOR to = new SipAOR(toContact.getUserName(), domain);
            toContact.attachTo(to);

            userAgent.getSipProvider().sendRequest(builder.createMessage(toContact, "nihao"));
            //ClientTransaction = userAgent.sendRequest(builder.createMessage(toContact, "nihao"));
        }

    }
}