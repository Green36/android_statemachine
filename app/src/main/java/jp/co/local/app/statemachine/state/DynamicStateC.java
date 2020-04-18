package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity;
import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.StateMachineException;
import jp.co.local.app.statemachine.controller.AppEventBase;
import jp.co.local.app.statemachine.controller.AppParam;

public class DynamicStateC extends Base {

    public DynamicStateC(){
        super(StateType.DYNAMIC_C);
        setChecker(StateType.DYNAMIC_C);
    }

    @Override
    protected StateType setInitialState() {
        return StateType.DYNAMIC_E;
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
    public void init()  throws StateMachineException {
        this.setStaticState(StateType.STATIC_D, new StaticStateD());

        this.setSubState(StateType.DYNAMIC_D, new DynamicStateD());
        this.setSubState(StateType.DYNAMIC_E, new DynamicStateE());
        this.setSubState(StateType.DYNAMIC_F, new DynamicStateF());

        {
            final MainActivity.EventType event = MainActivity.EventType.EVENT_A;
            this.setEvent(event, new AppEventBase() {
                @Override
                public StateType execute(MainActivity.EventType event, AppParam param) {
                    mChecker.exec(event, param);
                    return StateType.DYNAMIC_A;
                }
            });
        }
    }

}
