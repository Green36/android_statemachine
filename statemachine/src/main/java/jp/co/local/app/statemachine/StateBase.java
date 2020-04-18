package jp.co.local.app.statemachine;

import java.util.HashMap;
import java.util.Map;

/**
 * 状態クラス。
 * 複数の状態遷移をツリー構成で構成することで、ネストする状態間の遷移を実現する。<br>
 * {@link StateBase#init()} にて {@link StateBase#setStaticState(Enum, StateBase)}, {@link StateBase#setSubState(Enum, StateBase)} を実装することでツリー構成を構築する。<br>
 * また、同じく {@link StateBase#init()} にて {@link StateBase#setEvent(Enum, EventBase)} を実装することで、イベントハンドリング時の処理を実装する。<br>
 *
 * @param <S> 状態を識別するEnum
 * @param <E> イベントを識別するEnum
 */
public abstract class StateBase<S extends Enum<S>, E extends Enum<E>> {

    private Map<E, EventBase> mEvent;
    private Map<S, StateBase> mSubStateList;
    private Map<S, StateBase> mStaticState;
    private S mSubState;
    private S mInitialState;
    private S mThisState;

    protected StateBase() {}

    protected StateBase(S state) {
        mEvent = new HashMap<E, EventBase>();
        mSubStateList = new HashMap<S, StateBase>();
        mStaticState = new HashMap<S, StateBase>();
        mSubState = null;
        mInitialState = null;
        mThisState = state;
    }

    /**
     * 自状態以下に指定の状態が含まれるか確認する。
     * 静的状態へは遷移することはないのでチェックしない。
     *
     * @param state 指定の状態
     * @return true:含む false:含まない
     */
    private boolean contain(S state) {
        boolean ret = false;
        if (mSubStateList.containsKey(state)) {
            ret = true;
        } else {
            for (StateBase s : mSubStateList.values()) {
                if (s.contain(state)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * 自状態以下の指定状態へ遷移を行う
     * 静的状態以下へは遷移しない
     *
     * @param nextState 遷移先の状態
     * @return void
     * @throws StateMachineException : 遷移先が自状態以下に見つからない場合
     */
    private void transition(S nextState) throws StateMachineException {
        boolean transitioned = false;
        if (this.mSubStateList.containsKey(nextState)) {
            this.mSubState = nextState;
            this.mSubStateList.get(mSubState).entryState(this.mSubState);
            transitioned = true;
        } else {
            for (S s : mSubStateList.keySet()) {
                if (mSubStateList.get(s).contain(nextState)) {
                    mSubState = s;
                    mSubStateList.get(s).entryState(nextState);
                    transitioned = true;
                }
            }
        }
        if (transitioned == false) {
            throw new StateMachineException(String.valueOf(mThisState) + " is not contains " + String.valueOf(nextState));
        }
    }

    /**
     * イベントを発行し、各状態の該当するexecute()関数を実行する
     *
     * @param event 実行イベント
     * @param param 実行時のパラメータ
     * @return 実行後の遷移状態。遷移しない場合はnullが設定される
     * @throws StateMachineException 静的状態で遷移状態が設定されている場合
     */
    final S exec(E event, Object param) throws StateMachineException {
        S nextState = null;

        if (mEvent.containsKey(event)) {
            /* 自状態のイベントチェック */
            nextState = (S) mEvent.get(event).execute(event, param);
            if (nextState != null) {
                this.exitState();
            }
        } else {
            /* 静的状態へイベントを流す */
            for (StateBase s : mStaticState.values()) {
                /* 静的状態は状態遷移を行わないため戻り値はチェック不要 */
                if (s.exec(event, param) != null) {
                    throw new StateMachineException("Static status is prohibited from transition.");
                }
            }

            /* 動的状態へイベントを流す */
            if (mSubStateList != null && mSubStateList.get(mSubState) != null) {
                nextState = (S) mSubStateList.get(mSubState).exec(event, param);
            }
        }

        if (nextState != null && contain(nextState)) {
            /* 遷移：自状態配下に次状態がある場合 */
            this.transition(nextState);
            nextState = null;
        }

        return nextState;
    }

    /**
     * 自状態と、それに所属するサブ状態の init() を実行する
     *
     * @throws StateMachineException
     */
    final void initState() throws StateMachineException {
        init();
        S initState = setInitialState();
        if (initState != null) {
            if (mSubStateList.containsKey(initState)) {
                mInitialState = initState;
            } else {
                throw new StateMachineException("unexpected status:" + String.valueOf(initState));
            }
        } else if (mSubStateList.size() > 0) {
            throw new StateMachineException(String.valueOf(mThisState) + "have some status. but not set initial status.");
        }

        for (StateBase s : mStaticState.values()) {
            s.initState();
        }
        for (StateBase s : mSubStateList.values()) {
            s.initState();
        }
    }

    /**
     * 自状態と、それに所属するサブ状態の entry() を実行する
     *
     * @param nextState
     */
    final void entryState(S nextState) {
        this.entry();
        for (StateBase b : mStaticState.values()) {
            b.entryState(null);
        }

        if (nextState == mThisState) {
            this.mSubState = this.mInitialState;
        } else if (nextState == null) {
            this.mSubState = mInitialState;
        } else {
            this.mSubState = nextState;
        }

        if (this.mSubState != null) {
            this.mSubStateList.get(this.mSubState).entryState(this.mSubState);
        }
    }

    /**
     * 自状態と、それに所属するサブ状態の exit() を実行する
     */
    final void exitState() {
        for (StateBase b : mStaticState.values()) {
            b.exitState();
        }
        if (this.mSubState != null) {
            this.mSubStateList.get(this.mSubState).exitState();
        }
        this.exit();
    }

    /**
     * for debug
     *
     * @param parent
     * @return Status structure string
     */
    final String getStateString(String parent, int nestCount) {
        String indent = "";
        for (int i = 0; i <= nestCount; i++) {
            if (i == nestCount) {
                indent += " + ";
            } else {
                indent += " | ";
            }
        }
        String ret = "";

        for (S s : mStaticState.keySet()) {
            ret += "\n" + indent + s;
            ret += mStaticState.get(s).getStateString(ret, nestCount + 1);
        }
        if (this.mSubState != null) {
            ret += "\n" + indent + this.mSubState;
            parent += this.mSubStateList.get(mSubState).getStateString(ret, nestCount + 1);
        }
        return ret;
    }

    /**
     * 自状態がハンドリングするイベントを設定する。<br>
     * {@link StateBase#init()}内で実装すること
     *
     * @param eventEnum イベントを指すEnum
     * @param event     イベントクラス
     * @throws StateMachineException
     */
    final protected void setEvent(E eventEnum, EventBase event) throws StateMachineException {
        if (eventEnum == null) {
            throw new StateMachineException("e == null");
        }
        if (event == null) {
            throw new StateMachineException("event == null");
        }
        mEvent.put(eventEnum, event);
    }

    /**
     * 自状態がハンドリングする動的状態を設定する。<br>
     * @param s 状態を指す Enum値
     * @param state (動的)状態クラス
     * @throws StateMachineException
     */
    final protected void setSubState(S s, StateBase state) throws StateMachineException {
        if (s == null) {
            throw new StateMachineException("s == null");
        }
        mSubStateList.put(s, state);
    }

    /**
     * 自状態がハンドリングする静的状態を設定する<br>
     * @param s 状態を指す Enum値
     * @param state (静的)状態クラス
     * @throws StateMachineException
     */
    final protected void setStaticState(S s, StateBase state) throws StateMachineException {
        if (s == null) {
            throw new StateMachineException("s == null");
        }
        mStaticState.put(s, state);
    }

    /**
     * 本状態に遷移した際に実行される
     */
    abstract protected void entry();

    /**
     * 本状態から遷移する際に実行される
     */
    abstract protected void exit();

    /**
     * StateMachineクラスを生成した際に実行される。
     * 必要に応じて{@link StateBase#setSubState(Enum, StateBase)}, {@link StateBase#setStaticState(Enum, StateBase)}, {@link StateBase#setEvent(Enum, EventBase)} を実装する。
     *
     * @throws StateMachineException
     */
    abstract protected void init() throws StateMachineException;

    /**
     * 複数サブ状態を持つ場合、本状態に遷移した際に 有効にする、初期状態を指す Enum を return する。<br>
     * サブ状態を持たない場合は null を return する。<br>
     * @return　S 本状態に遷移した際に有効にするサブ状態を指す Enum値
     */
    abstract protected S setInitialState();
}
