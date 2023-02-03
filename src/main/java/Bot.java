import events.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import okhttp3.OkHttpClient;

import java.io.*;

public class Bot {
    public static JDA jda;
    
    public static void main(String[] args) throws Exception {
        InputStream tokenFile = Bot.class.getResourceAsStream("token.txt");
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(tokenFile));
        
        CommandManager cm = new CommandManager();
        
        jda = JDABuilder.create(fileReader.readLine(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .setActivity(Activity.playing("Lifesteal SMP"))
                .addEventListeners(cm)
                .build()
                .awaitReady();
        
        
        jda.updateCommands().addCommands(
                Commands.slash("greet", "Just a test command")
                        .addOption(OptionType.USER, "user", "The user that should be greeted"),
                Commands.slash("stop", "Shuts the bot down"))
                .queue();
        
        cm.addSlashCommandListener("greet", event -> {
            event.reply("Hi " + event.getOption("user").getAsUser().getName() + "!").queue();
            System.out.println("Greeted " + event.getOption("user").getAsUser().getName());
        });
        cm.addSlashCommandListener("stop", event -> {
            event.reply("Shutting down bot").queue();
            Bot.stop();
        });
    }
    
    public static void stop () {
        jda.getPresence().setPresence(OnlineStatus.OFFLINE, true);
        
        jda.shutdown();
        
        OkHttpClient client = jda.getHttpClient();
        client.connectionPool().evictAll();
        client.dispatcher().executorService().shutdown();
        
        System.exit(0);
    }
}
