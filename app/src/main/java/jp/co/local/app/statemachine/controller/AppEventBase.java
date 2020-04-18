package jp.co.local.app.statemachine.controller;

import jp.co.local.app.statemachine.MainActivity;
import jp.co.local.app.statemachine.EventBase;

public interface AppEventBase extends EventBase<MainActivity.StateType, MainActivity.EventType, AppParam> {
}
