package io.github.fisher2911.minionsplugin.util.property;

import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

public interface Property<T> {
    void set(@Nullable T value);

    void reset();

    void update(UnaryOperator<T> operator);

    @Nullable
    T get();

    static <T> Property<T> createAtomic(T value) {
        return new AtomicProperty<>(value);
    }

    static <T> Property<T> createAtomic() {
        return new AtomicProperty<>(null);
    }

    static <T> Property<T> create(T value) {
        return new SimpleProperty<>(value);
    }

    static <T> Property<T> create() {
        return new SimpleProperty<>(null);
    }
}
