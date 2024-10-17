package fr.naaranetwork.ffa.tasks;

import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import utils.TitleManager;

public class RespawnTask extends BukkitRunnable {

    private final FFA ffa;
    private final Player player;

    public RespawnTask(FFA ffa, Player player) {
        this.ffa = ffa;
        this.player = player;
    }

    @Override
    public void run() {

        if (player == null || !player.isOnline())
            return;

        ffa.getProfileHandler().handleSpawn(player);
        player.setGameMode(GameMode.SURVIVAL);
    }
}
