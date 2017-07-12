package bupt.androidsipchat.sip.util;

/*
 * Created by Maou on 2017/7/12.
 */

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class IPAddressHelper {

	public static String getIPAddress(Context context) {
		WifiManager wifiManager =
				(WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		// todo add permission to manifest

		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		int ipCode = wifiInfo.getIpAddress();
		return formatIpAddress(ipCode);
	}

	private static String formatIpAddress(int ipCode) {
		return (ipCode & 0xFF ) + "." +
			   ((ipCode >> 8 ) & 0xFF) + "." +
			   ((ipCode >> 16 ) & 0xFF) + "." +
			   ( ipCode >> 24 & 0xFF) ;
	}
}
