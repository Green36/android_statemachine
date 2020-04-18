package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity;
import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.StateMachineException;
import jp.co.local.app.statemachine.controller.AppEventBase;
import jp.co.local.app.statemachine.controller.AppParam;

public class StaticStateA extends Base {

    public StaticStateA(){
        super(StateType.STATIC_A);
        setChecker(StateType.STATIC_A);
    }

    @Override
    protected StateType setInitialState() {
        return StateType.DYNAMIC_M;
    }

    @Override
    public void entry() {
        super.entry();
    }

    @Override
    public void exit() {
        super.exit();
    }

    @Override
    public void init() throws StateMachineException {
        this.setStaticState(StateType.STATIC_B, new StaticStateB());

        this.setSubState(StateType.DYNAMIC_M, new DynamicStateM());
        this.setSubState(StateType.DYNAMIC_R, new DynamicStateR());

        final MainActivity.EventType event = MainActivity.EventType.EVENT_B;
        this.setEvent(event, new AppEventBase() {
            @Override
            public StateType execute(MainActivity.EventType event, AppParam param) {
                mChecker.exec(event, param);
                return null;
            }
        });
    }
}
