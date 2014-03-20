package com.uwflow.flow_android.network;

import android.os.AsyncTask;
import android.util.Log;

/**
 * This class is used to sync multiple async results
 * TODO: A TIME OUT SO IT DOESNT LOAD FOREVER
 */
public class FlowResultCollector {
    protected boolean processCompleteState[];
    protected ResultCollectorCallback callback;
    protected static final String COLLECTOR = "collector";

    public FlowResultCollector(int numProcess, ResultCollectorCallback callback) {
        this.processCompleteState = new boolean[numProcess];
        this.callback = callback;
    }

    public synchronized void setState(int index, boolean state) {
        Log.d(COLLECTOR, "Index: " + index);
        if (index >= 0 && index < processCompleteState.length) {
            processCompleteState[index] = state;
        }

        if (isAllProcessCompeted()) {
            callback.loadOrReloadCompleted();
            callback = null;
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

    // Timeout in 8 seconds
    public void startTimer() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (callback != null)
                    callback.loadOrReloadCompleted();
            }
        };
    }
}
