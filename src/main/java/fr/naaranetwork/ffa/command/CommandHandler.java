package fr.naaranetwork.ffa.command;

import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.command.defaults.SpawnCommand;
import org.bukkit.command.CommandExecutor;

public class CommandHandler {

    private final FFA ffa;

    public CommandHandler(FFA ffa) {
        this.ffa = ffa;
        this.loadCommands();
    }

    private void loadCommands() {
        registerCommand("spawn", new SpawnCommand(ffa));
    }

    public void registerCommand(String name, CommandExecutor executor) {
        ffa.getCommand(name).setExecutor(executor);
    }

}
