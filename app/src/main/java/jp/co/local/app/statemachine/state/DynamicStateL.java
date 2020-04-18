package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity;
import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.StateMachineException;
import jp.co.local.app.statemachine.controller.AppEventBase;
import jp.co.local.app.statemachine.controller.AppParam;

public class DynamicStateL extends Base {

    public DynamicStateL(){
        super(StateType.DYNAMIC_L);
        setChecker(StateType.DYNAMIC_L);
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

    @Override
    public void init() throws StateMachineException {
        {
            final MainActivity.EventType event = MainActivity.EventType.EVENT_D;
            this.setEvent(event, new AppEventBase() {
                @Override
                public StateType execute(MainActivity.EventType event, AppParam param) {
                    mChecker.exec(event, param);
                    return StateType.DYNAMIC_K;
                }
            });
        }
    }

}
