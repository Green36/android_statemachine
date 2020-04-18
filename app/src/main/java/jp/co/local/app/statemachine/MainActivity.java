package jp.co.local.app.statemachine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import jp.co.local.app.statemachine.state.Root;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "StateMachine";
    StateMachine mStateMachine;

    public enum EventType {
        EVENT_A,
        EVENT_B,
        EVENT_C,
        EVENT_D,
        EVENT_E,
        EVENT_F,
        EVENT_G,
        EVENT_H,
    }

    public enum StateType {
        STATE_ROOT,
        STATIC_A,
        STATIC_B,
        STATIC_C,
        STATIC_D,
        STATIC_E,
        DYNAMIC_A,
        DYNAMIC_B,
        DYNAMIC_C,
        DYNAMIC_D,
        DYNAMIC_E,
        DYNAMIC_F,
        DYNAMIC_G,
        DYNAMIC_H,
        DYNAMIC_I,
        DYNAMIC_J,
        DYNAMIC_K,
        DYNAMIC_L,
        DYNAMIC_M,
        DYNAMIC_N,
        DYNAMIC_O,
        DYNAMIC_P,
        DYNAMIC_Q,
        DYNAMIC_R,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Root rootState = new Root();
        mStateMachine = StateMachine.createInstance(Looper.getMainLooper(), rootState);
    }
}

