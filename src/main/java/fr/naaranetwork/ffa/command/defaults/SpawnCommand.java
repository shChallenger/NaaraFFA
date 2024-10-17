package fr.naaranetwork.ffa.command.defaults;

import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.tasks.TeleportTask;
import fr.naaranetwork.ffa.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final FFA ffa;

    public SpawnCommand(FFA ffa) {
        this.ffa = ffa;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (player.isFlying()) {
            ffa.getProfileHandler().handleSpawn(player);
            return true;
        }

        player.sendMessage(Utils.translate("&aTéléportation dans 5 secondes..."));

        TeleportTask teleportTask = new TeleportTask(player);
        teleportTask.runTaskTimer(ffa, 0, 20);

        return true;
    }
}
