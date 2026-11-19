package me.aiderlirk.crystalstaff;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.aiderlirk.crystalstaff.ability.CrystalBlastListener;
import me.aiderlirk.crystalstaff.command.CrystalStaffCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrystalStaffPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> event.registrar().register(
                        CrystalStaffCommand.create()
                )
        );

        getServer().getPluginManager().registerEvents(
                new CrystalBlastListener(this),
                this
        );

        getLogger().info("CrystalStaff enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("CrystalStaff disabled");
    }
}