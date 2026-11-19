package me.aiderlirk.crystalstaff.ability;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class CrystalShockwave {

    private static final double MAX_RADIUS = 5.0;
    private static final double RADIUS_STEP = 0.55;
    private static final int PARTICLE_POINTS = 40;

    private static final Particle.DustOptions WAVE_DUST =
            new Particle.DustOptions(
                    Color.fromRGB(155, 50, 255),
                    1.6f
            );

    private CrystalShockwave() {
    }

    public static void play(JavaPlugin plugin, Location origin) {

        World world = origin.getWorld();

        double centerX = origin.getX();
        double centerY = origin.getY() + 0.25;
        double centerZ = origin.getZ();

        new BukkitRunnable() {

            private double radius = 0.6;

            @Override
            public void run() {

                if (radius > MAX_RADIUS) {
                    cancel();
                    return;
                }

                for (int i = 0; i < PARTICLE_POINTS; i++) {

                    double angle =
                            2.0 * Math.PI * i / PARTICLE_POINTS;

                    double x = centerX + Math.cos(angle) * radius;
                    double z = centerZ + Math.sin(angle) * radius;

                    world.spawnParticle(
                            Particle.DUST,
                            x, centerY, z,
                            1,
                            0, 0, 0,
                            0,
                            WAVE_DUST
                    );
                }

                radius += RADIUS_STEP;
            }

        }.runTaskTimer(plugin, 0L, 2L);
    }
}