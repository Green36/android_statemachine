package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity.EventType;
import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.StateMachine;
import jp.co.local.app.statemachine.StateMachineException;
import jp.co.local.app.statemachine.controller.AppEventBase;
import jp.co.local.app.statemachine.controller.AppParam;

public class DynamicStateB extends Base {

    public DynamicStateB(){
        super(StateType.DYNAMIC_B);
        setChecker(StateType.DYNAMIC_B);
    }

    @Override
    protected StateType setInitialState()  {
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

    @Override
    public void init() throws StateMachineException {
        {
            final EventType event = EventType.EVENT_B;
            this.setEvent(event, new AppEventBase() {
                @Override
                public StateType execute(EventType event, AppParam param) {
                    mChecker.exec(event, param);
                    return StateType.DYNAMIC_C;
                }
            });
        }

        {
            final EventType event = EventType.EVENT_C;
            this.setEvent(event, new AppEventBase() {
                @Override
                public StateType execute(EventType event, AppParam param) {
                    mChecker.exec(event, param);
                    return StateType.DYNAMIC_D;
                }
            });
        }
    }

}
