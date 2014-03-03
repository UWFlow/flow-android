package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;

public abstract class FlowAbstractDataLoader<T extends Object> extends AsyncTaskLoader<T> {
    protected T mLastData = null;

    protected abstract T loadDelegate();
    protected FlowDatabaseHelper flowDatabaseHelper;

    public FlowAbstractDataLoader(Context context, FlowDatabaseHelper flowDatabaseHelper) {
        super(context);
        this.flowDatabaseHelper = flowDatabaseHelper;
    }

    /**
     * Runs on a worker thread, loading in our data. Delegates the real work to
     * concrete subclass' buildCursor() method.
     */
    @Override
    public T loadInBackground() {
        return loadDelegate();
    }

    /**
     * Runs on the UI thread, routing the results from the background thread to
     * whatever is using the dataList.
     */
    @Override
    public void deliverResult(T data) {
        if (isReset()) {
            data = null;
            return;
        }
        T oldDataList = mLastData;
        mLastData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
        if (oldDataList != null && oldDataList != data) {
            oldDataList = null;
        }
    }

    /**
     * Starts an asynchronous load of the list data. When the result is ready
     * the callbacks will be called on the UI thread. If a previous load has
     * been completed and is still valid the result may be passed to the
     * callbacks immediately.
     * <p/>
     * Must be called from the UI thread.
     */
    @Override
    protected void onStartLoading() {
        if (mLastData != null) {
            deliverResult(mLastData);
        }
        if (takeContentChanged() || mLastData == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread, triggered by a call to stopLoading().
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Must be called from the UI thread, triggered by a call to cancel(). Here,
     * we make sure our Cursor is closed, if it still exists and is not already
     * closed.
     */
    @Override
    public void onCanceled(T dataList) {
    }

    /**
     * Must be called from the UI thread, triggered by a call to reset(). Here,
     * we make sure our Cursor is closed, if it still exists and is not already
     * closed.
     */
    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
        mLastData = null;
    }
}