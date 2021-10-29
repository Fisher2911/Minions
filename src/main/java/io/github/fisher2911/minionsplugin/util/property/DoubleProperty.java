package io.github.fisher2911.minionsplugin.util.property;

import com.google.common.util.concurrent.AtomicDouble;
import io.github.fisher2911.minionsplugin.util.function.DoubleUnaryOperator;

public interface DoubleProperty {

    void add(double addend);

    void subtract(double subtrahend);

    void increment();

    void decrement();

    void reset();

    void set(double value);

    void update(DoubleUnaryOperator operator);

    double get();

    static DoubleProperty createAtomic(double value) {
        return new AtomicDoubleProperty(value);
    }

    static DoubleProperty createAtomic() {
        return new AtomicDoubleProperty(0d);
    }

    static DoubleProperty createPrecise(double value) {
        return new PreciseDoubleProperty(value);
    }

    static DoubleProperty createPrecise() {
        return new PreciseDoubleProperty(0d);
    }

    static DoubleProperty create(double value) {
        return new SimpleDoubleProperty(value);
    }

    static DoubleProperty create() {
        return new SimpleDoubleProperty(0d);
    }

}
