package fr.naaranetwork.ffa.utils;

import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.item.impl.SimpleItem;
import net.nilo.hologram.HologramAPI;
import net.nilo.hologram.IHologram;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Utils {

    public static final ItemStack[] INVENTORY = new ItemStack[36];
    public static final ItemStack[] ARMOR = new ItemStack[4];
    public static Location spawn = null;

    public static String translate(String text) {
        return text.replace("&", "§");
    }

    public static void clearPlayer(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setArmorContents(null);

        InventoryView inventoryView = player.getOpenInventory();

        if (inventoryView.getType() == InventoryType.CRAFTING) {
            for (byte i = 0; i < 5; i++) {
                inventoryView.setItem(i, null);
            }
        }

        player.setItemOnCursor(null);

        player.updateInventory();
    }

    public static void loadHologram() {
        HologramAPI.get().getHologramHandler().createHologram(spawn, "&eBienvenue à toi sur le FFA");
    }

    public static void loadInventory() {
        INVENTORY[0] = new ItemBuilder(Material.STONE_SWORD)
                .setName("§e§lEpée FFA")
                .setUnbreakable(true)
                .toItemStack();

        INVENTORY[1] = new ItemStack(Material.GOLDEN_APPLE);

        INVENTORY[8] = new SimpleItem(new ItemBuilder(Material.FEATHER).setName("§e§lChanger de forme").toItemStack(),
                player -> FFA.getInstance().getProfileHandler().handleSpectating(player),
                Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK).getIcon();

        ARMOR[3] = new ItemBuilder(Material.LEATHER_HELMET, 1)
                .setLeatherArmorColor((Color.YELLOW))
                .setName("§e§lCasque FFA")
                .setUnbreakable(true)
                .toItemStack();

        ARMOR[2] = new ItemBuilder(Material.IRON_CHESTPLATE)
                .setName("§e§lPlastron FFA")
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .setUnbreakable(true)
                .toItemStack();

        ARMOR[1] = new ItemBuilder(Material.LEATHER_LEGGINGS, 1)
                .setLeatherArmorColor(Color.YELLOW)
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .setName("§e§lPantalon FFA")
                .setUnbreakable(true)
                .toItemStack();

        ARMOR[0] = new ItemBuilder(Material.LEATHER_BOOTS, 1)
                .setLeatherArmorColor(Color.YELLOW)
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .setName("§e§lBottes FFA")
                .setUnbreakable(true)
                .toItemStack();
    }

    public static Location getLocationFromConfig(World world, String path) {
        FileConfiguration config = FFA.getInstance().getConfig();

        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");

        float yaw = (float) config.getDouble(path + ".yaw");
        float pitch = (float) config.getDouble(path + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

}
