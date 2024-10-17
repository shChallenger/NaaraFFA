package fr.naaranetwork.ffa.tasks;

import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportTask extends BukkitRunnable {

    private final Player player;
    private final Location startLocation;
    private int seconds;

    public TeleportTask(Player player) {
        this.seconds = 5;
        this.player = player;
        this.startLocation = player.getLocation();
    }

    @Override
    public void run() {

        if (player == null || !player.isOnline()) {
            cancel();
            return;
        }

        if (player.getLocation().getBlockX() != startLocation.getBlockX()
                || player.getLocation().getBlockZ() != startLocation.getBlockZ()) {
            player.sendMessage(Utils.translate("&cVous avez bougé, téléportation annulée."));
            cancel();
            return;
        }

        if (seconds < 1) {
            player.sendMessage(Utils.translate("&aVous avez été téléporté avec succès."));
            FFA.getInstance().getProfileHandler().handleSpawn(player);
            cancel();
        }

        seconds--;
    }
}
