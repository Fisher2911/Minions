package io.github.fisher2911.minionsplugin;

import io.github.fisher2911.fishcore.FishCore;
import io.github.fisher2911.fishcore.message.MessageHandlerRegistry;
import io.github.fisher2911.fishcore.user.UserManager;
import io.github.fisher2911.minionsplugin.command.CommandManager;
import io.github.fisher2911.minionsplugin.command.GiveMinionCommand;
import io.github.fisher2911.minionsplugin.command.PermissionsGroupCommand;
import io.github.fisher2911.minionsplugin.config.GuiLoader;
import io.github.fisher2911.minionsplugin.gui.GuiManager;
import io.github.fisher2911.minionsplugin.listener.BlockAddedToWorldListener;
import io.github.fisher2911.minionsplugin.listener.ChunkListener;
import io.github.fisher2911.minionsplugin.listener.EntityClickListener;
import io.github.fisher2911.minionsplugin.listener.EntityKillListener;
import io.github.fisher2911.minionsplugin.listener.MinionPlaceListener;
import io.github.fisher2911.minionsplugin.listener.PlayerJoinListener;
import io.github.fisher2911.minionsplugin.minion.data.MinionDataManager;
import io.github.fisher2911.minionsplugin.minion.food.FoodManager;
import io.github.fisher2911.minionsplugin.minion.inventory.EquipmentManager;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeGroupManager;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import io.github.fisher2911.minionsplugin.world.MinionConverter;

import java.util.HashMap;
import java.util.List;

public class MinionsPlugin extends FishCore {

    private UserManager<MinionUser> userManager;
    private GuiManager guiManager;
    private MinionManager minionManager;
    private UpgradeGroupManager upgradeGroupManager;
    private MinionConverter minionConverter;
    private MinionDataManager minionDataManager;
    private EquipmentManager equipmentManager;
    private FoodManager foodManager;
    private GuiLoader guiLoader;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        super.onEnable();
        this.initializeClasses();
        this.registerListeners();
        this.registerCommands();

        this.equipmentManager.load();
        this.foodManager.load();
        this.upgradeGroupManager.loadAll();
        this.minionDataManager.load();
        this.guiLoader.loadAll();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void initializeClasses() {
        this.upgradeGroupManager = new UpgradeGroupManager(new HashMap<>(), this);
        this.userManager = new UserManager<>();
        this.minionManager = new MinionManager(this);
        this.guiManager = new GuiManager();
        this.minionConverter = new MinionConverter(
                new HashMap<>(),
                this
        );
        this.minionDataManager = new MinionDataManager(this, new HashMap<>());
        this.equipmentManager = new EquipmentManager(new HashMap<>(), this);
        this.foodManager = new FoodManager(new HashMap<>(), this);
        this.guiLoader = new GuiLoader(this);
    }

    private void registerListeners() {
        List.of(new MinionPlaceListener(this),
                        new BlockAddedToWorldListener(this),
                        new EntityKillListener(this),
                        new EntityClickListener(this),
                        new ChunkListener(this),
                        new PlayerJoinListener(this)).
                forEach(this::registerListener);
    }

    private void registerCommands() {
        this.commandManager = new CommandManager("minion",
                MessageHandlerRegistry.REGISTRY.get(this.getClass()),
                        null);

        this.getCommand(this.commandManager.getName()).setExecutor(this.commandManager);

        this.commandManager.register(new GiveMinionCommand(this));
        this.commandManager.register(new PermissionsGroupCommand(this));

    }

    public UpgradeGroupManager getUpgradeGroupManager() {
        return this.upgradeGroupManager;
    }

    public UserManager<MinionUser> getUserManager() {
        return this.userManager;
    }

    public MinionManager getMinionManager() {
        return this.minionManager;
    }

    public MinionConverter getMinionConverter() {
        return this.minionConverter;
    }

    public MinionDataManager getMinionDataManager() {
        return this.minionDataManager;
    }

    public EquipmentManager getEquipmentManager() {
        return this.equipmentManager;
    }

    public FoodManager getFoodManager() {
        return this.foodManager;
    }

    public GuiManager getGuiManager() {
        return this.guiManager;
    }
}
