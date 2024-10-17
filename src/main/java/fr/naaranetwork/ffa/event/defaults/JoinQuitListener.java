package fr.naaranetwork.ffa.event.defaults;

import fr.naaranetwork.ffa.FFA;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final FFA ffa;

    public JoinQuitListener(FFA ffa) {
        this.ffa = ffa;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        ffa.getProfileHandler().handleJoin(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();

        ffa.getProfileHandler().handleQuit(player);
    }

}
