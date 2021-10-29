package io.github.fisher2911.minionsplugin.util.property;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

class AtomicIntProperty implements IntProperty {
    final AtomicInteger atomicInteger;

    AtomicIntProperty(int value) {
        this.atomicInteger = new AtomicInteger(value);
    }

    @Override
    public void add(int addend) {
        this.atomicInteger.getAndAdd(addend);
    }

    @Override
    public void subtract(int subtrahend) {
        this.atomicInteger.getAndAdd(-subtrahend);
    }

    @Override
    public void increment() {
        this.atomicInteger.getAndIncrement();
    }

    @Override
    public void decrement() {
        this.atomicInteger.getAndDecrement();
    }

    @Override
    public void reset() {
        this.atomicInteger.set(0);
    }

    @Override
    public void set(int value) {
        this.atomicInteger.set(value);
    }

    @Override
    public void update(IntUnaryOperator operator) {
        this.atomicInteger.getAndUpdate(operator);
    }

    @Override
    public int get() {
        return this.atomicInteger.get();
    }
}
