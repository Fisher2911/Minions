package io.github.fisher2911.minionsplugin.util.property;

import com.google.common.util.concurrent.AtomicDouble;
import io.github.fisher2911.minionsplugin.util.function.DoubleUnaryOperator;

class AtomicDoubleProperty implements DoubleProperty {
    final AtomicDouble atomicDouble;

    AtomicDoubleProperty(double value) {
        this.atomicDouble = new AtomicDouble(value);
    }

    @Override
    public void add(double addend) {
        this.atomicDouble.getAndAdd(addend);
    }

    @Override
    public void subtract(double subtrahend) {
        this.atomicDouble.getAndAdd(-subtrahend);
    }

    @Override
    public void increment() {
        this.atomicDouble.getAndAdd(1);
    }

    @Override
    public void decrement() {
        this.atomicDouble.getAndAdd(-1);
    }

    @Override
    public void reset() {
        this.atomicDouble.set(0);
    }

    @Override
    public void set(double value) {
        this.atomicDouble.set(value);
    }

    @Override
    public void update(DoubleUnaryOperator operator) {
        this.atomicDouble.set(operator.applyAsDouble(this.atomicDouble.get()));
    }

    @Override
    public double get() {
        return this.atomicDouble.get();
    }
}
