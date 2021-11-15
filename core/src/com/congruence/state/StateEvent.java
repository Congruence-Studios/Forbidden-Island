package com.congruence.state;

import java.util.Objects;

public class StateEvent {

    private int action;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateEvent that = (StateEvent) o;
        return action == that.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(action);
    }

    @Override
    public String toString() {
        return "StateEvent{" +
                "action=" + action +
                '}';
    }
}
