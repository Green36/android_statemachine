package jp.co.local.app.statemachine.state;

import jp.co.local.app.statemachine.MainActivity;
import jp.co.local.app.statemachine.StateBase;
import jp.co.local.app.statemachine.model.ExecChecker;

public abstract class Base extends StateBase<MainActivity.StateType, MainActivity.EventType> {

    ExecChecker.StateExecChecker mChecker;

    public Base(MainActivity.StateType s) {
        super(s);
    }

    protected void setChecker(MainActivity.StateType state){
        mChecker = ExecChecker.getInstance().get(state);
    }

    @Override
    public void entry() {
        mChecker.entry();
    }

    @Override
    public void exit() {
        mChecker.exit();
    }
}
