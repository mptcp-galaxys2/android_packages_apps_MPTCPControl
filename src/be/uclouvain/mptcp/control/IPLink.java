package be.uclouvain.mptcp.control;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.net.ConnectivityManager;
import android.util.Log;

public class IPLink {
	
	public static final int MULTIPATH_OFF = 0;
	public static final int MULTIPATH_ON = 1;
	public static final int MULTIPATH_BACKUP = 2;
	public static final int MULTIPATH_HANDOVER = 3;
	
	private static String execute(String cmd, boolean read) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream output = new DataOutputStream(process.getOutputStream());
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			output.writeBytes("ip link " + cmd + "\n");
			if (read)
				return input.readLine();
			else
				return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	private static String execute(String cmd) {
		return execute(cmd, true);
	}
	
	public static void set(int netType, int type) {
		IPLink.execute("set dev " + getInterfaceName(netType) + " multipath " + getCommand(type), false);
	}
	
	public static boolean isEnabled(int netType, int type) {
		String output = IPLink.execute("show dev " + getInterfaceName(netType));
		return output.indexOf(getDisplay(type)) > 0;
	}
	
	public static boolean isMPTCPEnabled(int netType) {
		return !isEnabled(netType, MULTIPATH_OFF) || isEnabled(netType, MULTIPATH_BACKUP) ||
				isEnabled(netType, MULTIPATH_HANDOVER);
	}
	
	private static String getCommand(int type) {
		switch (type) {
		case MULTIPATH_OFF:
			return "off";
		case MULTIPATH_ON:
			return "on";
		case MULTIPATH_BACKUP:
			return "backup";
		case MULTIPATH_HANDOVER:
			return "handover";
		default:
			return null;
		}
	}
	
	private static String getDisplay(int type) {
		switch (type) {
		case MULTIPATH_OFF:
			return "NOMULTIPATH";
		case MULTIPATH_ON:
			return "";
		case MULTIPATH_BACKUP:
			return "MPBACKUP";
		case MULTIPATH_HANDOVER:
			return "MPHANDOVER";
		default:
			return null;
		}
	}
	
	private static String getInterfaceName(int netType) {
		switch (netType) {
		case ConnectivityManager.TYPE_WIFI:
			return "wlan0";
		case ConnectivityManager.TYPE_MOBILE:
			return "rmnet0";
		default:
			return null;
		}
	}
	
}
