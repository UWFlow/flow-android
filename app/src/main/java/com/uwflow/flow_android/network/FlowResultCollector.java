package com.uwflow.flow_android.network;

import android.os.Handler;
import android.util.Log;
import com.crashlytics.android.Crashlytics;

/**
 * This class is used to sync multiple async results
 */
public class FlowResultCollector {
    private static final String TAG = FlowResultCollector.class.getSimpleName();

    protected boolean processCompleteState[];
    protected ResultCollectorCallback mCallback;
    protected static final String COLLECTOR = "collector";

    public FlowResultCollector(int numProcess, ResultCollectorCallback callback) {
        this.processCompleteState = new boolean[numProcess];
        mCallback = callback;
    }

    public synchronized void setState(int index, boolean state) {
        Log.d(COLLECTOR, "Index: " + index);
        if (index >= 0 && index < processCompleteState.length) {
            processCompleteState[index] = state;
        }

        if (isAllProcessCompeted() && mCallback != null) {
            ResultCollectorCallback callback = mCallback;
            mCallback = null;
            callback.loadOrReloadCompleted();
        }
    }

    public boolean getStateAt(int index) {
        if (index >= 0 && index < processCompleteState.length) {
            return processCompleteState[index];
        }
        return false;
    }

    public synchronized boolean isAllProcessCompeted() {
        for (boolean b : processCompleteState) {
            if (b == false)
                return false;
        }
        return true;
    }

    public void startTimer() {
        startTimer(null);
    }

    /**
     * Call this method if you require a timer for the callback. This means that
     * the callback will be called after delayMillis seconds regardless if the async calls are
     * successful or not.
     * @param delayMillis Milliseconds to wait before force-executing callback
     */
    public void startTimer(Long delayMillis) {
        final long finalDelayMillis = delayMillis == null ? 30000 : delayMillis;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    ResultCollectorCallback callback = mCallback;
                    mCallback = null;
                    callback.loadOrReloadCompleted();

                    // This is an abnormal situation, so log it
                    String message = "Async requests timed out after " + finalDelayMillis +
                            " millis. Callback called anyway.";
                    Log.w(TAG, message);
                    Crashlytics.log(Log.WARN, TAG, message);
                }
            }
        }, finalDelayMillis);
    }
}
