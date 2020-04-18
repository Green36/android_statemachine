package jp.co.local.app.statemachine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class StateMachine<E extends Enum<E>> {
    static final String TAG = "StateMachine";
    static StateMachine mInstance;
    private Handler mHandler;

    private StateBase mRootState;

    static public StateMachine createInstance(Looper looper, StateBase root) {
        if (mInstance != null) {
            Log.e(TAG, "already created");
            return null;
        }
        mInstance = new StateMachine(root);
        mInstance.init(looper);
        return mInstance;
    }

    static public StateMachine getInstatnace() throws StateMachineException {
        if (mInstance == null) {
            throw new StateMachineException("not created yet.");
        }
        return mInstance;
    }

    private StateMachine(StateBase root) {
        mRootState = root;
    }

    private void init(Looper looper) {
        try {
            mRootState.initState();
        } catch (StateMachineException e) {
            Log.e(TAG, "Exception");
            e.printStackTrace();
        }
        mRootState.entryState(null);

        mHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "handleMessage" + msg.toString());

                /* parameter check */
                if (msg.obj instanceof ParamBase) {
                    ParamBase param = (ParamBase) msg.obj;
                    try {
                        mRootState.exec(param.getEventEnum(), param.getParam());
                    } catch (StateMachineException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, mRootState.getStateString("ROOT STATE", 0));
            }
        };
    }


    public void finalize() {
        mRootState.exitState();
        mInstance = null;
    }

    public void postEvent(E event, Object param) {
        ParamBase parameter = new ParamBase(event);
        parameter.setParam(param);

        Message msg = new Message();
        msg.obj = parameter;
        mHandler.sendMessage(msg);
    }

    public class ParamBase<E extends Enum<E>> {
        private E event;
        private Object param;

        public ParamBase(E event) {
            this.event = event;
        }

        public void setParam(Object param) {
            this.param = param;
        }

        public Object getParam() {
            return this.param;
        }

        public E getEventEnum() {
            return this.event;
        }
    }
}
