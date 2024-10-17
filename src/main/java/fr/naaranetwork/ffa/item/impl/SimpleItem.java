package fr.naaranetwork.ffa.item.impl;

import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.item.AbstractItem;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SimpleItem extends AbstractItem {
    public SimpleItem(ItemStack icon, Consumer<Player> consumer, Action... actions) {
        super(icon, consumer, actions);

        FFA.getInstance().getItemHandler().registerItem(this);
    }
}
