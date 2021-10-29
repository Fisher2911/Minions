package io.github.fisher2911.minionsplugin.util.property;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

public class AtomicProperty<T> implements Property<T> {
    private final AtomicReference<T> atomicReferent;

    public AtomicProperty(T value) {
        this.atomicReferent = new AtomicReference<>(value);
    }

    @Override
    public void set(T value) {
        this.atomicReferent.set(value);
    }

    @Override
    public void reset() {
        this.atomicReferent.set(null);
    }

    @Override
    public void update(UnaryOperator<T> operator) {
        this.atomicReferent.getAndUpdate(operator);
    }

    @Override
    public T get() {
        return this.atomicReferent.get();
    }
}
