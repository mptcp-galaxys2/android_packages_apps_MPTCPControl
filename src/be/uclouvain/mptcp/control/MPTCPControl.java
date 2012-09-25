package be.uclouvain.mptcp.control;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.Switch;
import android.net.ConnectivityManager;

public class MPTCPControl extends Activity {

	public static final String KEY_MPTCP_ENABLED = "net.mptcp.mptcp_enabled";
	
	private MPTCPEnableSwitch mptcp_enable;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mptcpcontrol);
        
        mptcp_enable = (MPTCPEnableSwitch)findViewById(R.id.mptcp_enable);
        mptcp_enable.register(ConnectivityManager.TYPE_WIFI,
        		(Switch)findViewById(R.id.use_wifi),
        		(CheckBox)findViewById(R.id.wifi_backup));
        mptcp_enable.register(ConnectivityManager.TYPE_MOBILE,
        		(Switch)findViewById(R.id.use_data),
        		(CheckBox)findViewById(R.id.data_backup));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mptcpcontrol, menu);
        return true;
    }    
}
