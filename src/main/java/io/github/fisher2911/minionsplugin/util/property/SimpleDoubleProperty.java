package io.github.fisher2911.minionsplugin.util.property;

import io.github.fisher2911.minionsplugin.util.function.DoubleUnaryOperator;

class SimpleDoubleProperty implements DoubleProperty {
    double value;

    SimpleDoubleProperty(double value) {
        this.value = value;
    }

    @Override
    public void add(double addend) {
        this.value += addend;
    }

    @Override
    public void subtract(double subtrahend) {
        this.value -= subtrahend;
    }

    @Override
    public void increment() {
        this.value++;
    }

    @Override
    public void decrement() {
        this.value--;
    }

    @Override
    public void reset() {
        this.value = 0;
    }

    @Override
    public void set(double value) {
        this.value = value;
    }

    @Override
    public void update(DoubleUnaryOperator operator) {
        this.value = operator.applyAsDouble(this.value);
    }

    @Override
    public double get() {
        return this.value;
    }
}
