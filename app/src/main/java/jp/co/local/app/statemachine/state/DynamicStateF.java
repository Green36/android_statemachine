package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity;
import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.StateMachineException;
import jp.co.local.app.statemachine.controller.AppEventBase;
import jp.co.local.app.statemachine.controller.AppParam;

public class DynamicStateF extends Base {

    public DynamicStateF(){
        super(StateType.DYNAMIC_F);
        setChecker(StateType.DYNAMIC_F);
    }

    @Override
    protected StateType setInitialState() {
        return StateType.DYNAMIC_H;
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
        this.setStaticState(StateType.STATIC_D, new StaticStateE());

        this.setSubState(StateType.DYNAMIC_G, new DynamicStateG());
        this.setSubState(StateType.DYNAMIC_H, new DynamicStateH());

        {
            final MainActivity.EventType event = MainActivity.EventType.EVENT_E;
            this.setEvent(event, new AppEventBase() {
                @Override
                public StateType execute(MainActivity.EventType event, AppParam param) {
                    mChecker.exec(event, param);
                    return StateType.DYNAMIC_D;
                }
            });
        }
    }
}
