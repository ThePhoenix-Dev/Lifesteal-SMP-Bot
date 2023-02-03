package events;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.function.Consumer;

public class CommandManager extends ListenerAdapter {
    private HashMap<String, Consumer<SlashCommandInteractionEvent>> linkedSlashCommands = new HashMap<>();
    
    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {
        String command = event.getName().toLowerCase();
        if (linkedSlashCommands.containsKey(command)) {
            linkedSlashCommands.get(command).accept(event);
        }
    }
    
    public void addSlashCommandListener (String command, Consumer<SlashCommandInteractionEvent> callback) {
        linkedSlashCommands.put(command.toLowerCase(), callback);
    }
    public void removeSlashCommandListener (String command) {
        linkedSlashCommands.remove(command);
    }
}
