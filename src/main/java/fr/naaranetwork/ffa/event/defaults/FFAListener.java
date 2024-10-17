package fr.naaranetwork.ffa.event.defaults;

import database.profiles.FFAProfile;
import database.profiles.ProfileState;
import fr.naaranetwork.ffa.FFA;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class FFAListener implements Listener {

    private final FFA ffa;

    public FFAListener(FFA ffa) {
        this.ffa = ffa;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER || event.getDamager().getType() != EntityType.PLAYER) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        FFAProfile damagerProfile = FFA.getInstance().getProfileHandler().getProfile(damager.getUniqueId());
        FFAProfile profile = FFA.getInstance().getProfileHandler().getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.PLAYING || damagerProfile.getState() != ProfileState.PLAYING) {
            event.setCancelled(true);
            return;
        }

        profile.setLastDamager(damager);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity();
        FFAProfile profile = FFA.getInstance().getProfileHandler().getProfile(player.getUniqueId());
        event.setKeepInventory(true);
        event.setDroppedExp(0);

        Player lastDamager = profile.getLastDamager();

        ffa.getProfileHandler().handleDeath(player, lastDamager, false);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
