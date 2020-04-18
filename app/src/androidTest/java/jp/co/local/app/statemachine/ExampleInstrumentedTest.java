package jp.co.local.app.statemachine;

import android.os.Looper;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import jp.co.local.app.statemachine.controller.AppParam;
import jp.co.local.app.statemachine.model.ExecChecker;
import jp.co.local.app.statemachine.state.Root;
import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.MainActivity.EventType;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    StateMachine stateMachine;

    class ExpectedValue {
        private StateType mStateType;
        private ExecChecker.ExeType mExeType;
        private EventType mEventType;
        private AppParam mParam;
        private int mSeqNum;

        private ExpectedValue() {
        }

        public ExpectedValue(StateType state, ExecChecker.ExeType exetype, EventType eventType, AppParam param, int seq) {
            mStateType = state;
            mExeType = exetype;
            mEventType = eventType;
            mParam = param;
            mSeqNum = seq;
        }
    }

    List<ExpectedValue> expectedTrueList;

    public void initExpected() {
        expectedTrueList = new ArrayList<ExpectedValue>();
    }

    public void addExpected(ExpectedValue expected) {
        expectedTrueList.add(expected);
    }

    private void isExecuted(StateType s, ExecChecker.ExeType type, EventType eventType, AppParam param) {
        boolean hit = false;
        int seqNum = 0;
        for (ExpectedValue expected : expectedTrueList) {
            if ((s == expected.mStateType) && (expected.mExeType == type)) {
                if (expected.mExeType == ExecChecker.ExeType.EXECUTE) {
                    if (eventType == expected.mEventType) {
                        hit = true;
                        seqNum = expected.mSeqNum;
                        Log.i("TEST", "##### " + s + ", " + type + ", " + eventType + ", param " + param.getParam() + " #####");
                        assertEquals(ExecChecker.getInstance().get(s).param(eventType).getParam(), param.getParam());
                    }
                } else {
                    hit = true;
                    seqNum = expected.mSeqNum;
                }
            }
        }
        Log.i("TEST", "##### " + s + ", " + type + ", " + eventType + ", sequense:" + seqNum + " #####");
        if (hit) {
            assertEquals(seqNum, ExecChecker.getInstance().isTrue(s, type, eventType));
        } else {
            assertEquals(0, ExecChecker.getInstance().isTrue(s, type, eventType));
        }
    }

    public void assertAll(AppParam param) {
        for (StateType s : StateType.values()) {
            isExecuted(s, ExecChecker.ExeType.ENTRY, null, null);
            isExecuted(s, ExecChecker.ExeType.EXIT, null, null);
            for (EventType e : EventType.values()) {
                isExecuted(s, ExecChecker.ExeType.EXECUTE, e, param);
            }
        }
    }

    @Before
    public void setup() {
        ExecChecker.getInstance().get(StateType.STATE_ROOT).isEntry();
        Root rootState = new Root();
//        rootState.setStateId(StateType.STATE_ROOT);
//        rootState.init();
        stateMachine = StateMachine.createInstance(Looper.getMainLooper(), rootState);
    }

    public void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void useAppContext() {
        AppParam param = new AppParam();
        EventType event = null;
        initExpected();
        /************************************************************/
        addExpected(new ExpectedValue(StateType.STATE_ROOT, ExecChecker.ExeType.ENTRY, null, null, 1));
        addExpected(new ExpectedValue(StateType.STATIC_A, ExecChecker.ExeType.ENTRY, null, null, 2));
        addExpected(new ExpectedValue(StateType.STATIC_B, ExecChecker.ExeType.ENTRY, null, null,3));
        addExpected(new ExpectedValue(StateType.STATIC_C, ExecChecker.ExeType.ENTRY, null, null,4));
        addExpected(new ExpectedValue(StateType.DYNAMIC_Q, ExecChecker.ExeType.ENTRY, null, null,5));
        addExpected(new ExpectedValue(StateType.DYNAMIC_N, ExecChecker.ExeType.ENTRY, null, null,6));
        addExpected(new ExpectedValue(StateType.DYNAMIC_M, ExecChecker.ExeType.ENTRY, null, null,7));
        addExpected(new ExpectedValue(StateType.DYNAMIC_A, ExecChecker.ExeType.ENTRY, null, null, 8));
        /************************************************************/
        ExecChecker.getInstance().dumpResult();
        assertAll(null);

        ExecChecker.getInstance().clear();
        initExpected();

        param.setParam(1);
        event = EventType.EVENT_A;
        stateMachine.postEvent(event, param);
        sleep();
        /************************************************************/
        addExpected(new ExpectedValue(StateType.DYNAMIC_Q, ExecChecker.ExeType.EXECUTE, event, param,1));
        addExpected(new ExpectedValue(StateType.DYNAMIC_Q, ExecChecker.ExeType.EXIT, null, null,2));
        addExpected(new ExpectedValue(StateType.DYNAMIC_P, ExecChecker.ExeType.ENTRY, null, null,3));

        addExpected(new ExpectedValue(StateType.DYNAMIC_N, ExecChecker.ExeType.EXECUTE, event, param,4));
        addExpected(new ExpectedValue(StateType.DYNAMIC_N, ExecChecker.ExeType.EXIT, null, null,5));
        addExpected(new ExpectedValue(StateType.DYNAMIC_O, ExecChecker.ExeType.ENTRY, null, null,6));

        addExpected(new ExpectedValue(StateType.DYNAMIC_M, ExecChecker.ExeType.EXECUTE, event, param,7));
        addExpected(new ExpectedValue(StateType.DYNAMIC_M, ExecChecker.ExeType.EXIT, null, null,8));
        addExpected(new ExpectedValue(StateType.DYNAMIC_R, ExecChecker.ExeType.ENTRY, null, null,9));

        addExpected(new ExpectedValue(StateType.DYNAMIC_A, ExecChecker.ExeType.EXECUTE, event, param, 10));
        addExpected(new ExpectedValue(StateType.DYNAMIC_A, ExecChecker.ExeType.EXIT, null, null, 11));
        addExpected(new ExpectedValue(StateType.DYNAMIC_B, ExecChecker.ExeType.ENTRY, null, null,12));
        /************************************************************/
        ExecChecker.getInstance().dumpResult();
        assertAll(param);

        ExecChecker.getInstance().clear();
        initExpected();

        param.setParam(2);
        event = EventType.EVENT_B;
        stateMachine.postEvent(event, param);
        sleep();
        /************************************************************/
        addExpected(new ExpectedValue(StateType.STATIC_A, ExecChecker.ExeType.EXECUTE, event, param,1));

        addExpected(new ExpectedValue(StateType.DYNAMIC_B, ExecChecker.ExeType.EXECUTE, event, param,2));
        addExpected(new ExpectedValue(StateType.DYNAMIC_B, ExecChecker.ExeType.EXIT, null, null,3));
        addExpected(new ExpectedValue(StateType.DYNAMIC_C, ExecChecker.ExeType.ENTRY, null, null,4));
        addExpected(new ExpectedValue(StateType.STATIC_D, ExecChecker.ExeType.ENTRY, null, null,5));
        addExpected(new ExpectedValue(StateType.DYNAMIC_K, ExecChecker.ExeType.ENTRY, null, null,6));
        addExpected(new ExpectedValue(StateType.DYNAMIC_E, ExecChecker.ExeType.ENTRY, null, null,7));
        /************************************************************/
        ExecChecker.getInstance().dumpResult();
        assertAll(param);

        ExecChecker.getInstance().clear();
        initExpected();

        param.setParam(3);
        event = EventType.EVENT_C;
        stateMachine.postEvent(event, param);
        sleep();
        /***********************************************************/
        addExpected(new ExpectedValue(StateType.DYNAMIC_P, ExecChecker.ExeType.EXECUTE, event, param,1));
        addExpected(new ExpectedValue(StateType.DYNAMIC_P, ExecChecker.ExeType.EXIT, null, null,2));
        addExpected(new ExpectedValue(StateType.DYNAMIC_Q, ExecChecker.ExeType.ENTRY, null, null,3));

        addExpected(new ExpectedValue(StateType.DYNAMIC_R, ExecChecker.ExeType.EXECUTE, event, param,4));
        addExpected(new ExpectedValue(StateType.DYNAMIC_R, ExecChecker.ExeType.EXIT, null, null,5));
        addExpected(new ExpectedValue(StateType.DYNAMIC_M, ExecChecker.ExeType.ENTRY, null, null,6));

        addExpected(new ExpectedValue(StateType.DYNAMIC_K, ExecChecker.ExeType.EXECUTE, event, param,7));
        addExpected(new ExpectedValue(StateType.DYNAMIC_K, ExecChecker.ExeType.EXIT, null, null,8));
        addExpected(new ExpectedValue(StateType.DYNAMIC_L, ExecChecker.ExeType.ENTRY, null, null,9));

        addExpected(new ExpectedValue(StateType.DYNAMIC_E, ExecChecker.ExeType.EXECUTE, event, param,10));
        addExpected(new ExpectedValue(StateType.DYNAMIC_E, ExecChecker.ExeType.EXIT, null, null,11));
        addExpected(new ExpectedValue(StateType.DYNAMIC_F, ExecChecker.ExeType.ENTRY, null, null,12));
        addExpected(new ExpectedValue(StateType.STATIC_E, ExecChecker.ExeType.ENTRY, null, null,13));
        addExpected(new ExpectedValue(StateType.DYNAMIC_I, ExecChecker.ExeType.ENTRY, null, null,14));
        addExpected(new ExpectedValue(StateType.DYNAMIC_G, ExecChecker.ExeType.ENTRY, null, null,15));
        /************************************************************/
        ExecChecker.getInstance().dumpResult();
        assertAll(param);

        ExecChecker.getInstance().clear();
        initExpected();

        param.setParam(4);
        event = EventType.EVENT_D;
        stateMachine.postEvent(event, param);
        sleep();
        /***********************************************************/
        addExpected(new ExpectedValue(StateType.DYNAMIC_O, ExecChecker.ExeType.EXECUTE, event, param,1));
        addExpected(new ExpectedValue(StateType.DYNAMIC_O, ExecChecker.ExeType.EXIT, null, null,2));
        addExpected(new ExpectedValue(StateType.DYNAMIC_N, ExecChecker.ExeType.ENTRY, null, null,3));

        addExpected(new ExpectedValue(StateType.DYNAMIC_L, ExecChecker.ExeType.EXECUTE, event, param,4));
        addExpected(new ExpectedValue(StateType.DYNAMIC_L, ExecChecker.ExeType.EXIT, null, null,5));
        addExpected(new ExpectedValue(StateType.DYNAMIC_K, ExecChecker.ExeType.ENTRY, null, null,6));

        addExpected(new ExpectedValue(StateType.DYNAMIC_I, ExecChecker.ExeType.EXECUTE, event, param,7));
        addExpected(new ExpectedValue(StateType.DYNAMIC_I, ExecChecker.ExeType.EXIT, null, null,8));
        addExpected(new ExpectedValue(StateType.DYNAMIC_J, ExecChecker.ExeType.ENTRY, null, null,9));

        addExpected(new ExpectedValue(StateType.DYNAMIC_G, ExecChecker.ExeType.EXECUTE, event, param,10));
        addExpected(new ExpectedValue(StateType.DYNAMIC_G, ExecChecker.ExeType.EXIT, null, null,11));
        addExpected(new ExpectedValue(StateType.DYNAMIC_H, ExecChecker.ExeType.ENTRY, null, null,12));
        /************************************************************/
        ExecChecker.getInstance().dumpResult();
        assertAll(param);

        ExecChecker.getInstance().clear();
        initExpected();

        param.setParam(5);
        event = EventType.EVENT_A;
        stateMachine.postEvent(event, param);
        sleep();
        /***********************************************************/
        addExpected(new ExpectedValue(StateType.DYNAMIC_Q, ExecChecker.ExeType.EXECUTE, event, param,1));
        addExpected(new ExpectedValue(StateType.DYNAMIC_Q, ExecChecker.ExeType.EXIT, null, null,2));
        addExpected(new ExpectedValue(StateType.DYNAMIC_P, ExecChecker.ExeType.ENTRY, null, null,3));

        addExpected(new ExpectedValue(StateType.DYNAMIC_N, ExecChecker.ExeType.EXECUTE, event, param,4));
        addExpected(new ExpectedValue(StateType.DYNAMIC_N, ExecChecker.ExeType.EXIT, null, null,5));
        addExpected(new ExpectedValue(StateType.DYNAMIC_O, ExecChecker.ExeType.ENTRY, null, null,6));

        addExpected(new ExpectedValue(StateType.DYNAMIC_M, ExecChecker.ExeType.EXECUTE, event, param,7));
        addExpected(new ExpectedValue(StateType.DYNAMIC_M, ExecChecker.ExeType.EXIT, null, null,8));
        addExpected(new ExpectedValue(StateType.DYNAMIC_R, ExecChecker.ExeType.ENTRY, null, null,9));

        addExpected(new ExpectedValue(StateType.DYNAMIC_C, ExecChecker.ExeType.EXECUTE, event, param, 10));
        addExpected(new ExpectedValue(StateType.DYNAMIC_K, ExecChecker.ExeType.EXIT, null, null, 11));
        addExpected(new ExpectedValue(StateType.STATIC_D, ExecChecker.ExeType.EXIT, null, null, 12));
        addExpected(new ExpectedValue(StateType.DYNAMIC_J, ExecChecker.ExeType.EXIT, null, null, 13));
        addExpected(new ExpectedValue(StateType.STATIC_E, ExecChecker.ExeType.EXIT, null, null, 14));
        addExpected(new ExpectedValue(StateType.DYNAMIC_H, ExecChecker.ExeType.EXIT, null, null, 15));
        addExpected(new ExpectedValue(StateType.DYNAMIC_F, ExecChecker.ExeType.EXIT, null, null, 16));
        addExpected(new ExpectedValue(StateType.DYNAMIC_C, ExecChecker.ExeType.EXIT, null, null, 17));
        addExpected(new ExpectedValue(StateType.DYNAMIC_A, ExecChecker.ExeType.ENTRY, null, null,18));
        /************************************************************/
        ExecChecker.getInstance().dumpResult();
        assertAll(param);

        ExecChecker.getInstance().clear();
        initExpected();

        param.setParam(6);
        event = EventType.EVENT_A;
        stateMachine.postEvent(event, param);
        sleep();
        /***********************************************************/
        addExpected(new ExpectedValue(StateType.DYNAMIC_A, ExecChecker.ExeType.EXECUTE, event, param,1));
        addExpected(new ExpectedValue(StateType.DYNAMIC_A, ExecChecker.ExeType.EXIT, null, null,2));
        addExpected(new ExpectedValue(StateType.DYNAMIC_B, ExecChecker.ExeType.ENTRY, null, null,3));
        /************************************************************/
        ExecChecker.getInstance().dumpResult();
        assertAll(param);

        ExecChecker.getInstance().clear();
        initExpected();

        param.setParam(7);
        event = EventType.EVENT_B;
        stateMachine.postEvent(event, param);
        sleep();
        /***********************************************************/
        addExpected(new ExpectedValue(StateType.STATIC_A, ExecChecker.ExeType.EXECUTE, event, param,1));

        addExpected(new ExpectedValue(StateType.DYNAMIC_B, ExecChecker.ExeType.EXECUTE, event, param,2));
        addExpected(new ExpectedValue(StateType.DYNAMIC_B, ExecChecker.ExeType.EXIT, null, null,3));
        addExpected(new ExpectedValue(StateType.DYNAMIC_C, ExecChecker.ExeType.ENTRY, null, null,4));

        addExpected(new ExpectedValue(StateType.STATIC_D, ExecChecker.ExeType.ENTRY, null, null,5));
        addExpected(new ExpectedValue(StateType.DYNAMIC_K, ExecChecker.ExeType.ENTRY, null, null,6));
        addExpected(new ExpectedValue(StateType.DYNAMIC_E, ExecChecker.ExeType.ENTRY, null, null,7));
        /************************************************************/
        ExecChecker.getInstance().dumpResult();
        assertAll(param);

        ExecChecker.getInstance().clear();
        initExpected();

        stateMachine.finalize();
        sleep();
        /***********************************************************/
        addExpected(new ExpectedValue(StateType.DYNAMIC_P, ExecChecker.ExeType.EXIT, null, null,1));
        addExpected(new ExpectedValue(StateType.STATIC_C, ExecChecker.ExeType.EXIT, null, null,2));
        addExpected(new ExpectedValue(StateType.DYNAMIC_O, ExecChecker.ExeType.EXIT, null, null,3));
        addExpected(new ExpectedValue(StateType.STATIC_B, ExecChecker.ExeType.EXIT, null, null,4));
        addExpected(new ExpectedValue(StateType.DYNAMIC_R, ExecChecker.ExeType.EXIT, null, null,5));
        addExpected(new ExpectedValue(StateType.STATIC_A, ExecChecker.ExeType.EXIT, null, null,6));

        addExpected(new ExpectedValue(StateType.DYNAMIC_K, ExecChecker.ExeType.EXIT, null, null,7));
        addExpected(new ExpectedValue(StateType.STATIC_D, ExecChecker.ExeType.EXIT, null, null,8));
        addExpected(new ExpectedValue(StateType.DYNAMIC_E, ExecChecker.ExeType.EXIT, null, null,9));
        addExpected(new ExpectedValue(StateType.DYNAMIC_C, ExecChecker.ExeType.EXIT, null, null,10));
        addExpected(new ExpectedValue(StateType.STATE_ROOT, ExecChecker.ExeType.EXIT, null, null,11));
        /************************************************************/
        ExecChecker.getInstance().dumpResult();
        assertAll(param);
    }
}
