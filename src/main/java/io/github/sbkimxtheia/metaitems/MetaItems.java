package io.github.sbkimxtheia.metaitems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
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
	
	public static void Log(String... msg){
		for (String s : msg) {
			 String message = prefix + ChatColor.WHITE + " " + s;
			 logger.sendMessage(message);
		}
	}
}
