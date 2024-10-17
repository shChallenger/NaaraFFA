package fr.naaranetwork.ffa.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;

public class ItemHandler {

    private final Map<Class<? extends AbstractItem>, AbstractItem> items;

    public ItemHandler() {
        this.items = Maps.newHashMap();
        this.loadItems();
    }

    private void loadItems() {
        
    }

    public void registerItem(AbstractItem item) {
        Preconditions.checkNotNull(item, "Item cannot be null");

        items.put(item.getClass(), item);
    }

    public Map<Class<? extends AbstractItem>, AbstractItem> getItems() {
        return items;
    }
}
