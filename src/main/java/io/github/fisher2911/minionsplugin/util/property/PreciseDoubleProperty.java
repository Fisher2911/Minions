package io.github.fisher2911.minionsplugin.util.property;

import io.github.fisher2911.minionsplugin.util.function.DoubleUnaryOperator;
import io.github.fisher2911.minionsplugin.util.property.DoubleProperty;

import java.math.BigDecimal;

class PreciseDoubleProperty implements DoubleProperty {
    BigDecimal bigDecimal;

    PreciseDoubleProperty(double value) {
        this.bigDecimal = BigDecimal.valueOf(value);
    }

    @Override
    public void add(double addend) {
        this.bigDecimal = this.bigDecimal.add(BigDecimal.valueOf(addend));
    }

    @Override
    public void subtract(double subtrahend) {
        this.bigDecimal = this.bigDecimal.subtract(BigDecimal.valueOf(subtrahend));
    }

    @Override
    public void increment() {
        this.bigDecimal = this.bigDecimal.add(BigDecimal.valueOf(1));
    }

    @Override
    public void decrement() {
        this.bigDecimal = this.bigDecimal.add(BigDecimal.valueOf(-1));
    }

    @Override
    public void reset() {
        this.bigDecimal = BigDecimal.valueOf(0d);
    }

    @Override
    public void set(double value) {
        this.bigDecimal = BigDecimal.valueOf(value);
    }

    @Override
    public void update(DoubleUnaryOperator operator) {
        this.bigDecimal = BigDecimal.valueOf(operator.applyAsDouble(this.bigDecimal.doubleValue()));
    }

    @Override
    public double get() {
        return this.bigDecimal.doubleValue();
    }
}
