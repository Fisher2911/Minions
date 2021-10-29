package io.github.fisher2911.minionsplugin.util.property;

import java.util.function.UnaryOperator;

public class SimpleProperty<T> implements Property<T> {
    T value;

    SimpleProperty(T value) {
        this.value = value;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }

    @Override
    public void reset() {
        this.value = null;
    }

    @Override
    public void update(UnaryOperator<T> operator) {
        this.value = operator.apply(this.value);
    }

    @Override
    public T get() {
        return this.value;
    }
}
