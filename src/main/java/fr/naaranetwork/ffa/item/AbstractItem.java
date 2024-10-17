package fr.naaranetwork.ffa.item;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractItem {

    private final ItemStack icon;
    private final Consumer<Player> consumer;
    private final List<Action> actions;

    protected AbstractItem(ItemStack icon, Consumer<Player> consumer,  Action... actions) {
        this.icon = icon;
        this.consumer = consumer;
        this.actions = List.of(actions);
    }

    public void onClick(Player player) {
        consumer.accept(player);
    }

    public ItemStack getIcon() {
        return icon;
    }

    public List<Action> getActions() {
        return actions;
    }
}
