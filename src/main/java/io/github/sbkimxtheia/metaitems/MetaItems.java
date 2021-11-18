package io.github.sbkimxtheia.metaitems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class MetaItems extends JavaPlugin {
	
	private static final String prefix = ChatColor.AQUA + "[MetaItems]";
	public static MetaItems plugin;
	public static ConsoleCommandSender logger;
	
	@Override
	public void onEnable () {
		plugin = this;
		logger = Bukkit.getServer().getConsoleSender();
		
		Log("Hello, World!");
	}
	
	@Override
	public void onDisable () {
		// Plugin shutdown logic
	}
	
	public static void Log(String... message){
		for (String s : message) {
			 String _msg_ = prefix + ChatColor.WHITE + " " + s;
			 logger.sendMessage(_msg_);
		}
	}
	
	public static void Message(CommandSender player, String... message){
		for (String s : message) {
			String _msg_ = prefix + ChatColor.WHITE + " " + s;
			player.sendMessage(message);
		}
	}
}
