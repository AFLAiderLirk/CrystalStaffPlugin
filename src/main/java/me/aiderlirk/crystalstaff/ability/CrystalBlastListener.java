package me.aiderlirk.crystalstaff.ability;

import me.aiderlirk.crystalstaff.item.CrystalStaffItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class CrystalBlastListener implements Listener {

    private static final double RADIUS = 5.0;
    private static final double KNOCKBACK = 1.6;
    private static final int COOLDOWN_TICKS = 100;

    private static final Particle.DustOptions PURPLE_DUST =
            new Particle.DustOptions(
                    Color.fromRGB(170, 70, 255),
                    1.5f
            );

    private static final Component COOLDOWN_MESSAGE =
            MiniMessage.miniMessage().deserialize(
                    "<gray>Кристалл перезаряжается..."
            );

    private final JavaPlugin plugin;

    public CrystalBlastListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR
                && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (!CrystalStaffItem.isCrystalStaff(item)) {
            return;
        }

        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);

        Player player = event.getPlayer();

        if (player.hasCooldown(item)) {
            player.sendActionBar(COOLDOWN_MESSAGE);
            return;
        }

        player.setCooldown(item, COOLDOWN_TICKS);

        castBlast(player);
    }

    private void castBlast(Player player) {
        Location origin = player.getLocation();
        Location center = origin.clone().add(0, 1, 0);
        World world = player.getWorld();
        CrystalShockwave.play(plugin, origin);

        world.spawnParticle(
                Particle.DUST,
                center,
                120,
                2.5, 1.0, 2.5,
                0,
                PURPLE_DUST
        );

        world.spawnParticle(
                Particle.END_ROD,
                center,
                30,
                2.0, 0.8, 2.0,
                0.08
        );

        world.playSound(
                center,
                Sound.ENTITY_EVOKER_CAST_SPELL,
                1.0f,
                1.2f
        );

        Vector originVector = origin.toVector();

        for (Entity entity : player.getNearbyEntities(
                RADIUS, RADIUS, RADIUS
        )) {

            if (!(entity instanceof Monster monster)) {
                continue;
            }

            if (monster.getLocation().distanceSquared(origin)
                    > RADIUS * RADIUS) {
                continue;
            }

            Vector direction = monster.getLocation()
                    .toVector()
                    .subtract(originVector);

            direction.setY(0);

            if (direction.lengthSquared() < 0.01) {
                direction = origin.getDirection().setY(0);
            }

            direction.normalize()
                    .multiply(KNOCKBACK)
                    .setY(0.5);

            monster.setVelocity(direction);
        }
    }
}
