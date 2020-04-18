package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity;
import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.StateMachineException;
import jp.co.local.app.statemachine.controller.AppEventBase;
import jp.co.local.app.statemachine.controller.AppParam;

public class StaticStateB extends Base {

    public StaticStateB(){
        super(StateType.STATIC_B);
        setChecker(StateType.STATIC_B);
    }

    @Override
    protected StateType setInitialState() {
        return StateType.DYNAMIC_N;
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
        this.setStaticState(StateType.STATIC_C, new StaticStateC());

        this.setSubState(StateType.DYNAMIC_N, new DynamicStateN());
        this.setSubState(StateType.DYNAMIC_O, new DynamicStateO());

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
