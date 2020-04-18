package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.MainActivity.EventType;
import jp.co.local.app.statemachine.StateMachineException;
import jp.co.local.app.statemachine.controller.AppEventBase;
import jp.co.local.app.statemachine.controller.AppParam;

public class DynamicStateA extends Base {

    public DynamicStateA(){
        super(StateType.DYNAMIC_A);
        setChecker(StateType.DYNAMIC_A);
    }

    @Override
    public void init() throws StateMachineException {
        final EventType eventA = EventType.EVENT_A;
        this.setEvent(eventA, new AppEventBase() {
            @Override
            public StateType execute(EventType event, AppParam param) {
                mChecker.exec(eventA, param);
                return StateType.DYNAMIC_B;
            }
        });
    }

    @Override
    protected StateType setInitialState() {
        return null;
    }

    @Override
    public void entry() {
        super.entry();
    }

    @Override
    public void exit() {
        super.exit();
    }
}
