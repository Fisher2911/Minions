package io.github.fisher2911.minionsplugin.storage;

import io.github.fisher2911.minionsplugin.user.MinionUser;
import io.github.fisher2911.minionsplugin.util.function.ThrowingRunnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public record Storage(StorageHandler storageHandler, Executor storageExecutor) {
    private static RuntimeException transformToRuntimeException(Exception e) {
        return e instanceof RuntimeException ex ? ex : new RuntimeException(e);
    }

    public void boot() {
        this.storageHandler.boot();
    }

    private CompletableFuture<Void> runWithStorageExecutor(ThrowingRunnable<Exception> runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw Storage.transformToRuntimeException(e);
            }
        },this.storageExecutor);
    }

    public CompletableFuture<Void> loadUser(MinionUser user) {
        return this.runWithStorageExecutor(() -> this.storageHandler.loadUser(user));
    }

    public CompletableFuture<Void> saveUser(MinionUser user) {
        return this.runWithStorageExecutor(() -> this.storageHandler.saveUser(user));
    }
}
