package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.StateMachineException;

public class StaticStateC extends Base {

    public StaticStateC(){
        super(StateType.STATIC_C);
        setChecker(StateType.STATIC_C);
    }

    @Override
    protected StateType setInitialState() {
        return StateType.DYNAMIC_Q;
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
        this.setSubState(StateType.DYNAMIC_P, new DynamicStateP());
        this.setSubState(StateType.DYNAMIC_Q, new DynamicStateQ());
    }
}
