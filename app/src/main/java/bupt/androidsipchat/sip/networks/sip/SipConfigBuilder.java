package bupt.androidsipchat.sip.networks.sip;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import bupt.androidsipchat.sip.util.Configuration;

/*
 * Created by Maou Lim on 2017/7/8.
 */
public class SipConfigBuilder {

    public static final String PROPERTY_STACK_NAME = "android.javax.sip.STACK_NAME";
    public static final String PROPERTY_IP_ADDRESS = "android.javax.sip.IP_ADDRESS";
    public static final String PROPERTY_TRACE_LEVEL = "android.gov.nist.javax.sip.TRACE_LEVEL";
    public static final String PROPERTY_SERVER_LOG = "android.gov.nist.javax.sip.SERVER_LOG";
    public static final String PROPERTY_DEBUG_LOG = "android.gov.nist.javax.sip.DEBUG_LOG";

    public static Properties readFrom(Configuration config) {
        Properties properties = new Properties();

        if (config.containKey(PROPERTY_STACK_NAME)) {
            properties.setProperty(
                    PROPERTY_STACK_NAME,
                    (String) config.get(PROPERTY_STACK_NAME)
            );
        }

        if (config.containKey(PROPERTY_IP_ADDRESS)) {
            properties.setProperty(
                    PROPERTY_IP_ADDRESS,
                    (String) config.get(PROPERTY_IP_ADDRESS)
            );
        } else {
            try {
                properties.setProperty(
                        PROPERTY_IP_ADDRESS,
                        //Inet4Address.getLocalHost().getHostAddress()
                        InetAddress.getLocalHost().getHostAddress()
                );
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
        }

        if (config.containKey(PROPERTY_TRACE_LEVEL)) {
            properties.setProperty(
                    PROPERTY_TRACE_LEVEL,
                    (String) config.get(PROPERTY_TRACE_LEVEL)
            );
        }

        if (config.containKey(PROPERTY_SERVER_LOG)) {
            properties.setProperty(
                    PROPERTY_SERVER_LOG,
                    (String) config.get(PROPERTY_SERVER_LOG)
            );
        }

        if (config.containKey(PROPERTY_DEBUG_LOG)) {
            properties.setProperty(
                    PROPERTY_DEBUG_LOG,
                    (String) config.get(PROPERTY_DEBUG_LOG)
            );
        }

        return properties;
    }
}
