package bupt.androidsipchat.sip.networks.sip;


import android.javax.sip.address.Address;
import android.javax.sip.address.AddressFactory;
import android.javax.sip.header.CSeqHeader;
import android.javax.sip.header.ContactHeader;
import android.javax.sip.header.HeaderFactory;
import android.javax.sip.message.Request;
import android.javax.sip.message.Response;

import java.text.ParseException;

/*
 * Created by Maou Lim on 2017/7/10.
 */
public class SipResponseBuilder {

    private static SipFactoryHelper sipFactoryHelper = SipFactoryHelper.getInstance();
    private static AddressFactory addressFactory = sipFactoryHelper.getAddressFactory();
    private static HeaderFactory headerFactory = sipFactoryHelper.getHeaderFactory();

    private SipUserAgent sipUserAgent = null;

    public SipResponseBuilder(SipUserAgent sipUserAgent) {
        this.sipUserAgent = sipUserAgent;
    }

    public Response create(Request request, int statusCode) throws ParseException {
        Response response =
                sipFactoryHelper.getMessageFactory().createResponse(statusCode, request);

        Address contact = sipUserAgent.getContactAOR().getSipAddress();
        ContactHeader contactHeader = headerFactory.createContactHeader(contact);
        response.addHeader(contactHeader);

        return response;
    }

    public static long getResponseCSeq(Response response) {
        CSeqHeader cSeqHeader = (CSeqHeader) response.getHeader(CSeqHeader.NAME);

        if (null == cSeqHeader) {
            return -1;
        }

        return cSeqHeader.getSeqNumber();
    }
}
