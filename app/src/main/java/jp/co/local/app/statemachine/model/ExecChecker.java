package jp.co.local.app.statemachine.model;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import jp.co.local.app.statemachine.MainActivity.EventType;
import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.controller.AppParam;

public class ExecChecker {
    private final String TAG = "ExecChecker";
    private static ExecChecker mMe;
    Map<StateType, StateExecChecker> mMap = new HashMap<StateType, StateExecChecker>();
    private int mSeq = 0;
    public int sequence(){
        return ++mSeq;
    }

    public enum ExeType{
        ENTRY,
        EXECUTE,
        EXIT
    }

    public int isTrue(StateType status, ExeType exec, EventType event){
        int ret = 0;
        switch(exec) {
            case ENTRY:
                ret = mMap.get(status).isEntry();
                break;
            case EXECUTE:
                ret = mMap.get(status).isExec(event);
                break;
            case EXIT:
                ret = mMap.get(status).isExit();
                break;
        }
        return ret;
    }

    private ExecChecker(){
        for(StateType s : StateType.values()){
            mMap.put(s, new StateExecChecker());
        }
    }

    static public ExecChecker getInstance(){
        if(mMe == null){
            mMe = new ExecChecker();
        }
        return mMe;
    }

    public void clear(){
        for(StateType s : StateType.values()){
            mMap.get(s).clear();
        }
        mSeq = 0;
    }

    @NonNull
    public StateExecChecker get(StateType s){
        return mMap.get(s);
    }

    public void dumpResult(){
        Log.e(TAG, "###########################################");
        //for(StateExecChecker checker : mMap.values()){
        for (StateType s : mMap.keySet()) {
            mMap.get(s).dumpResult( s);
        }
        Log.e(TAG, "###########################################");
    }

    public class StateExecChecker {
        static final String TAG = "StateExecChecker";
        public int exec_entry = 0;
        public int exec_exit = 0;
        public Map<EventType, Integer> execMap = new HashMap<EventType, Integer>();
        public Map<EventType, AppParam> paramObj = new HashMap<EventType, AppParam>();

        public StateExecChecker(){
            for( EventType e : EventType.values()){
                execMap.put(e, 0);
                paramObj.put(e,null);
            }
        }

        public void clear(){
            exec_entry = 0;
            exec_exit = 0;
            for( EventType e : EventType.values()){
                execMap.put(e, 0);
                paramObj.put(e, null);
            }
        }
        public void dumpResult(StateType s) {
            if( exec_entry>0) {
                Log.e(TAG, String.valueOf(s) + " entry = " + exec_entry);
            }
            if(exec_exit > 0) {
                Log.e(TAG, String.valueOf(s) + " exit = " + exec_exit);
            }

            for (EventType event : execMap.keySet()) {
                if( execMap.get(event)>0) {
                    Log.e(TAG, String.valueOf(s) + " " + String.valueOf(event) + " = " + execMap.get(event) + ", param = " /*+ paramObj.get(event).getParam()*/);
                }
            }
        }

        public void exec(EventType e, AppParam param){
            execMap.put(e, sequence());
            paramObj.put(e, param);
        }
        public int isExec(EventType e){
            return execMap.get(e);
        }

        public AppParam param(EventType e){
            return paramObj.get(e);
        }

        public void entry(){
            exec_entry = sequence();
        }
        public int isEntry(){
            return exec_entry;
        }

        public void exit(){
            exec_exit = sequence();
        }
        public int isExit(){
            return exec_exit;
        }
    }
}
