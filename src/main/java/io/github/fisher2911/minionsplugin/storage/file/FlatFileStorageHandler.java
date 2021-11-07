package io.github.fisher2911.minionsplugin.storage.file;

import io.github.fisher2911.minionsplugin.storage.StorageHandler;
import io.github.fisher2911.minionsplugin.user.MinionUser;

import java.nio.file.Path;

public class FlatFileStorageHandler implements StorageHandler {
    private final Path userPath;

    public FlatFileStorageHandler(Path userPath) {
        this.userPath = userPath;
    }

    @Override
    public void boot() {

    }

    @Override
    public void saveUser(MinionUser user) throws Exception {
    }

    @Override
    public void loadUser(MinionUser user) throws Exception {

    }
}
