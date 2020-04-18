package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.StateMachineException;

public class StaticStateE extends Base {

    public StaticStateE(){
        super(StateType.STATIC_E);
        setChecker(StateType.STATIC_E);
    }

    @Override
    protected StateType setInitialState() {
        return StateType.DYNAMIC_I;
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
        this.setSubState(StateType.DYNAMIC_I, new DynamicStateI());
        this.setSubState(StateType.DYNAMIC_J, new DynamicStateJ());
    }
}
