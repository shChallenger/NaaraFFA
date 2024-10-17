package fr.naaranetwork.ffa.tasks;

import database.profiles.FFAProfile;
import fr.malgret.board.IPacketBoard;
import fr.naaranetwork.ffa.FFA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import utils.ScoreboardMaster;

public class AnimationTask extends BukkitRunnable {
    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            IPacketBoard packetBoard = player.getBoard();
            FFAProfile profile = FFA.getInstance().getProfileHandler().getProfile(player.getUniqueId());
            packetBoard.setLine(0, profile.getScoreboardAnimation().getAnimatedAddress());
        }

    }
}
