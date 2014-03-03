package com.uwflow.flow_android.network;

/**
 * This class is used to sync multiple async results
 */
public class FlowResultCollector {
    protected boolean processCompleteState[];
    protected ResultCollectorCallback callback;

    public FlowResultCollector(int numProcess, ResultCollectorCallback callback){
        this.processCompleteState = new boolean[numProcess];
        this.callback = callback;
    }

    public void setState(int index, boolean state){
        if (index >= 0 && index < processCompleteState.length){
            processCompleteState[index] = state;
        }

        if (isAllProcessCompeted()){
            callback.loadOrReloadCompleted();
        }
    }

    public boolean getStateAt(int index){
        if (index >= 0 && index < processCompleteState.length){
            return processCompleteState[index];
        }
        return false;
    }

    public boolean isAllProcessCompeted(){
        for (boolean b : processCompleteState){
            if (b == false)
                return false;
        }
        return true;
    }
}
