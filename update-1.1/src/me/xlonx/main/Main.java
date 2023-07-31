package me.xlonx.main;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.testhc.Utility.ChatUtility;


public class Main extends JavaPlugin implements Listener {

    public boolean chat_enable = true;

    @Override
    public void onEnable() {
        getLogger().info("[Serwer] Plugin wlaczony");

        saveDefaultConfig();

        reloadConfig();

        getCommand("chat").setExecutor(this);
        getCommand("ChatReload").setExecutor(this);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("[Serwer] Plugin wylaczony");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        FileConfiguration config = this.getConfig();
        if (!chat_enable) {
            if (!event.getPlayer().hasPermission("chat.bypass")) {
                event.setCancelled(true);
                
                event.getPlayer().sendMessage(ChatUtility.McColor(config.getString("disable")));
            }
        }
        
        String message = event.getMessage().toLowerCase();
		List<String> blockedWords = config.getStringList("blocked-words");
		
		for (String blockedWord : blockedWords)
		{
			if(message.contains(blockedWord.toLowerCase()))
			{
				if(!event.getPlayer().hasPermission("chat.bypass"))
				{
					event.setCancelled(true);
					
					event.getPlayer().sendMessage(ChatUtility.McColor(config.getString("eword_blocked")));
				}
			}
		}
		
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = this.getConfig();
        if (command.getName().equalsIgnoreCase("chat")) {
            Player p = (Player) sender;
            
            if(p.hasPermission("chat.use"))
            {
            	if (args.length == 1) {
            		if (args[0].equalsIgnoreCase("on")) {
            			chat_enable = true;
            			p.sendMessage(ChatUtility.McColor(config.getString("shelf_message")));
            			getServer().broadcastMessage(ChatUtility.McColor(config.getString("server2_message")));
            		} else if (args[0].equalsIgnoreCase("off")) {
            			chat_enable = false;
            			p.sendMessage(ChatUtility.McColor(config.getString("shelf2_message")));
            			getServer().broadcastMessage(ChatUtility.McColor(config.getString("server_message")));
            		} else if (args[0].equalsIgnoreCase("clear"))
            		{
            			for (int i = 0; i < 300; i++)
            			{
            				getServer().broadcastMessage("");
            			}
            			p.sendMessage(ChatUtility.McColor(config.getString("shelf_clear")));
            			getServer().broadcastMessage(ChatUtility.McColor(config.getString("server_clear")));
            		}
            		else
            		{
            			p.sendMessage(ChatUtility.McColor(config.getString("usage")));
            		}
            	}
            	else
            	{
            		p.sendMessage(ChatUtility.McColor(config.getString("usage")));
            	}
            }
            else
            {
            	p.sendMessage(ChatUtility.McColor(config.getString("not_permission")));
            }
        } else if (command.getName().equalsIgnoreCase("ChatReload")) {
        	Player p = (Player) sender;
        	if(p.hasPermission("chat.reload"))
        	{
        		reloadConfig();
        		p.sendMessage(ChatUtility.McColor(config.getString("reload")));
        	}
        	else
        	{
        		p.sendMessage(ChatUtility.McColor(config.getString("not_permission")));
        	}
        }
        return true;
    }
}