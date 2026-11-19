package me.aiderlirk.crystalstaff.item;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.UseCooldownComponent;
import org.bukkit.persistence.PersistentDataType;

public final class CrystalStaffItem {

    private static final MiniMessage MINI_MESSAGE =
            MiniMessage.miniMessage();

    private static final NamespacedKey MODEL_KEY =
            new NamespacedKey("crystalstaff", "crystal_staff");

    private static final NamespacedKey STAFF_KEY =
            new NamespacedKey("crystalstaff", "staff_id");

    private static final NamespacedKey COOLDOWN_KEY =
            new NamespacedKey("crystalstaff", "blast_cooldown");

    private CrystalStaffItem() {
    }

    public static ItemStack create() {
        ItemStack staff = new ItemStack(Material.BLAZE_ROD);

        ItemMeta meta = staff.getItemMeta();

        meta.itemName(
                MINI_MESSAGE.deserialize("<light_purple>Кристальный посох")
        );

        meta.setItemModel(MODEL_KEY);

        UseCooldownComponent cooldown = meta.getUseCooldown();
        cooldown.setCooldownGroup(COOLDOWN_KEY);
        cooldown.setCooldownSeconds(5.0f);
        meta.setUseCooldown(cooldown);

        staff.setItemMeta(meta);

        staff.editPersistentDataContainer(pdc ->
                pdc.set(STAFF_KEY, PersistentDataType.BYTE, (byte) 1)
        );

        return staff;
    }

    public static boolean isCrystalStaff(ItemStack item) {
        return item != null
                && item.getType() == Material.BLAZE_ROD
                && item.getPersistentDataContainer().has(
                        STAFF_KEY,
                        PersistentDataType.BYTE
                );
    }
}