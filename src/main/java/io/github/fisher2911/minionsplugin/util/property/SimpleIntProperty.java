package io.github.fisher2911.minionsplugin.util.property;

import java.util.function.IntUnaryOperator;

class SimpleIntProperty implements IntProperty {
    int value;

    SimpleIntProperty(int value) {
        this.value = value;
    }

    @Override
    public void add(int addend) {
        this.value += addend;
    }

    @Override
    public void subtract(int subtrahend) {
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
    public void set(int value) {
        this.value = value;
    }

    @Override
    public void update(IntUnaryOperator operator) {
        this.value = operator.applyAsInt(this.value);
    }

    @Override
    public int get() {
        return this.value;
    }
}
