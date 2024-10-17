package fr.naaranetwork.ffa.event.defaults;

import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.item.AbstractItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    private final FFA ffa;

    public ItemListener(FFA ffa) {
        this.ffa = ffa;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        for (AbstractItem abstractItem : ffa.getItemHandler().getItems().values()) {
            if (!abstractItem.getIcon().isSimilar(item) || !abstractItem.getActions().contains(event.getAction())) {
                continue;
            }

            abstractItem.onClick(player);
        }
    }

}
