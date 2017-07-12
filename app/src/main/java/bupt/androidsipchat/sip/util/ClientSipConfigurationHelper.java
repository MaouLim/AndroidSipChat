package bupt.androidsipchat.sip.util;

/*
 * Created by Maou on 2017/7/12.
 */

import android.content.Context;

public class ClientSipConfigurationHelper {

	public static final String PROPERTY_STACK_NAME = "android.javax.sip.STACK_NAME";
	public static final String PROPERTY_IP_ADDRESS = "android.javax.sip.IP_ADDRESS";

	private static Configuration config = null;

	private Context context = null;

	public ClientSipConfigurationHelper(Context context) {
		this.context = context;
	}

	public Configuration createConfiguration() {
		Configuration config = new Configuration();

		config.put(PROPERTY_STACK_NAME, "AndroidSipClient");
		config.put(PROPERTY_IP_ADDRESS, IPAddressHelper.getIPAddress(this.context));

		return config;
	}
}
