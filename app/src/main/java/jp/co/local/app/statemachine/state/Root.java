package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.StateMachineException;

public class Root extends Base {

    public Root(){
        super(StateType.STATE_ROOT);
        setChecker(StateType.STATE_ROOT);
    }

    @Override
    protected StateType setInitialState() {
        return StateType.DYNAMIC_A;
    }

    @Override
    public void init() throws StateMachineException {

        this.setStaticState(StateType.STATIC_A, new StaticStateA());

        this.setSubState(StateType.DYNAMIC_A, new DynamicStateA());
        this.setSubState(StateType.DYNAMIC_B, new DynamicStateB());
        this.setSubState(StateType.DYNAMIC_C, new DynamicStateC());
    }
}
