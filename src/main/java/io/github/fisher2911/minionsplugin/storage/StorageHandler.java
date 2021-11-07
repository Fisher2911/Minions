package io.github.fisher2911.minionsplugin.storage;

import io.github.fisher2911.minionsplugin.user.MinionUser;

public interface StorageHandler {
    void boot();

    void saveUser(MinionUser user) throws Exception;

    void loadUser(MinionUser user) throws Exception;
}
