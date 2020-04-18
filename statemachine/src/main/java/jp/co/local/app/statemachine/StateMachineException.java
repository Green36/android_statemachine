package jp.co.local.app.statemachine;

public class StateMachineException extends Exception {
    private String msg;

    public StateMachineException(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
