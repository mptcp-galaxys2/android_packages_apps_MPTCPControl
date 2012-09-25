package be.uclouvain.mptcp.control;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Switch;

public class MPTCPEnableSwitch extends Switch implements OnClickListener {
	
	public static final String KEY_MPTCP_ENABLED = "net.mptcp.mptcp_enabled";
	public ArrayList<DataSwitch> listeners;
	
	public MPTCPEnableSwitch(Context ctx) {
		super(ctx);
		init();
	}
	
	public MPTCPEnableSwitch(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		init();
	}
	
	public MPTCPEnableSwitch(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);
		init();
	}
	
	private void init() {
		listeners = new ArrayList<DataSwitch>();
		setOnClickListener(this);
		this.setChecked(isEnabled());
		enableListeners(isEnabled());
	}
	
	public void onClick(View v) {
		setEnabled(!isEnabled());
	}
	
	public boolean isEnabled() {
		return Sysctl.getInt(KEY_MPTCP_ENABLED) == 1;
	}
	
	public void setEnabled(boolean enabled) {
		Sysctl.set(KEY_MPTCP_ENABLED, enabled);
		enableListeners(enabled);
	}
	
	public void register(int netType, Switch on_off, CheckBox backup) {
		listeners.add(new DataSwitch(netType, on_off, backup));
	}
	
	private void enableListeners(boolean enabled) {
		for (DataSwitch data : listeners)
			data.setEnabled(enabled);
	}
	
	private class DataSwitch {
		private Switch on_off;
		private CheckBox backup;

		public DataSwitch(final int netType, Switch on_off, final CheckBox backup) {
			this.on_off = on_off;
			this.backup = backup;

			on_off.setChecked(IPLink.isMPTCPEnabled(netType));
			backup.setChecked(IPLink.isEnabled(netType, IPLink.MULTIPATH_BACKUP));
			backup.setEnabled(on_off.isChecked());
			
			on_off.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					if (IPLink.isMPTCPEnabled(netType)) {
						IPLink.set(netType, IPLink.MULTIPATH_OFF);
						backup.setEnabled(false);
					} else {
						IPLink.set(netType, IPLink.MULTIPATH_ON);
						backup.setEnabled(true);
					}
					backup.setChecked(false);
				}
			});
			
			backup.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (IPLink.isEnabled(netType, IPLink.MULTIPATH_BACKUP))
						IPLink.set(netType, IPLink.MULTIPATH_ON);
					else
						IPLink.set(netType, IPLink.MULTIPATH_BACKUP);
				}
			});
		}
		
		public void setEnabled(boolean enabled) {
			on_off.setEnabled(enabled);
			backup.setEnabled(enabled);
		}
	}

	
}
