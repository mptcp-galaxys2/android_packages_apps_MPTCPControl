package be.uclouvain.mptcp.control;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Sysctl {

	private static String execute(final String cmd) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream output = new DataOutputStream(process.getOutputStream());
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			output.writeBytes(cmd + "\n");
			return input.readLine();
		} catch (IOException e) {
			return null;
		}
	}
	
	public static void set(String key, String value) {
		Sysctl.execute("sysctl -w " + key + "=" + value);
	}
	
	public static void set(String key, int value) {
		Sysctl.set(key, new Integer(value).toString());
	}

	public static void set(String key, boolean value) {
		Sysctl.set(key, value ? 1 : 0);
	}
	
	public static int getInt(String key) {
		return Integer.parseInt(Sysctl.execute("sysctl " + key).split(" ")[2]);
	}
	
}
