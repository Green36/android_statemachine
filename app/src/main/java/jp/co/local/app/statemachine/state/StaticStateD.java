package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity.StateType;
import jp.co.local.app.statemachine.StateMachineException;

public class StaticStateD extends Base {

    public StaticStateD(){
        super(StateType.STATIC_D);
        setChecker(StateType.STATIC_D);
    }

    @Override
    protected StateType setInitialState() {
        return StateType.DYNAMIC_K;
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
        this.setSubState(StateType.DYNAMIC_K, new DynamicStateK());
        this.setSubState(StateType.DYNAMIC_L, new DynamicStateL());
    }

}
