package fr.naaranetwork.ffa.tasks;

import database.profiles.FFAProfile;
import database.profiles.ProfileState;
import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.scoreboard.IndexOption;
import fr.naaranetwork.ffa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import utils.ActionBarManager;

public class FFATask extends BukkitRunnable {

    private final FFA ffa;

    public FFATask(FFA ffa) {
        this.ffa = ffa;
    }

    @Override
    public void run() {

        int players = Bukkit.getOnlinePlayers().size();
        int spectators = ffa.getProfileHandler().getSpectatorsSize();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.SURVIVAL) {
                continue;
            }

            FFAProfile profile = ffa.getProfileHandler().getProfile(player.getUniqueId());
            if (profile.getState() == ProfileState.WAITING
                    && (Utils.spawn.getBlockY() - player.getLocation().getBlockY()) > 3) {

                player.getInventory().remove(Material.FEATHER);

                profile.setState(ProfileState.PLAYING);
                ActionBarManager.sendActionBarMessage(player, Utils.translate("&aVous entrez dans l'arène !"));
            }

            if (player.getLocation().getY() <= 30) {
                if (profile.getState() == ProfileState.SPECTATING) {
                    ffa.getProfileHandler().handleSpawn(player);
                } else {
                    ffa.getProfileHandler().handleDeath(player, null, false);
                }
            }

            player.getBoard().setValue(IndexOption.PLAYERS,
                    players);
        }

        ffa.getProfileHandler().updateRanks();
    }
}
