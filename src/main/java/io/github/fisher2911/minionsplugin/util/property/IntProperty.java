package io.github.fisher2911.minionsplugin.util.property;

import java.util.function.IntUnaryOperator;

public interface IntProperty {

    void add(int addend);

    void subtract(int subtrahend);

    void increment();

    void decrement();

    void reset();

    void set(int value);

    void update(IntUnaryOperator operator);

    int get();

    static IntProperty createAtomic(int value) {
        return new AtomicIntProperty(value);
    }

    static IntProperty createAtomic() {
        return new AtomicIntProperty(0);
    }

    static IntProperty create(int value) {
        return new SimpleIntProperty(value);
    }

    static IntProperty create() {
        return new SimpleIntProperty(0);
    }

}
