package io.github.fisher2911.minionsplugin.util.function;

public interface ThrowingRunnable<E extends Exception> {
    void run() throws E;
}
