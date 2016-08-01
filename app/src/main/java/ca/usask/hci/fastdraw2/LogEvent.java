package ca.usask.hci.fastdraw2;

import org.json.JSONException;
import org.json.JSONObject;

public class LogEvent extends JSONObject {

	private StudyLogger logger;
	
	public LogEvent(StudyLogger logger) {
		this.logger = logger;
	}

	public <T> LogEvent attr(String k, T v) {
		try {
			this.put(k, v);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this; // so multiple calls can be chained
	}
	
	public void commit() {
		logger.commitLogEvent(this);
	}
	
}
