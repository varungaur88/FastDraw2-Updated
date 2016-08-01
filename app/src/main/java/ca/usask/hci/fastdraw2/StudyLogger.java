package ca.usask.hci.fastdraw2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

public class StudyLogger {
	
	private final static String TAG = StudyLogger.class.getSimpleName();

	private static StudyLogger instance = null;

	public static StudyLogger getInstance() {
		if (instance == null) {
			instance = new StudyLogger();
		}
		return instance;
	}

	private File mLogDir;
	private File mLogFile;

    private String mLogFileName;
	private long mSessionStartTimestamp;
    private String mSessionStartDate;

	protected StudyLogger() {
		mLogDir = DrawingApp.logDir;
	}

	public void startOrResumeLog(String name) {
		mLogFileName = name + ".log";
        mLogFile = new File(mLogDir, mLogFileName);
        mSessionStartDate = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss", Locale.US).format(new Date());
        mSessionStartTimestamp = SystemClock.uptimeMillis();
        if (!mLogFile.exists()) {
            createLogEvent("event.startlogging").commit();
        } else {
            createLogEvent("event.resumelogging").commit();
        }
	}

	public LogEvent createLogEvent(String type) {
		long ts = SystemClock.uptimeMillis();
		return new LogEvent(this).attr("type", type)
				.attr("sessionstartdate", mSessionStartDate)
				.attr("sessionstartts", mSessionStartTimestamp)
				.attr("timestamp", ts);
	}

    public void commitLogEvent(LogEvent e) {
        writeLogEvent(e);
    }

	public synchronized void writeLogEvent(LogEvent evt) {
		Log.d(TAG, evt.toString());
		try {
			FileWriter fw = new FileWriter(mLogFile, true);
			fw.append(evt.toString() + "###\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
