package jp.co.local.app.statemachine;

public interface EventBase<S extends Enum<S>, E extends Enum<E>, P> {
    public S execute(E event, P param);
}
