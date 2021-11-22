package io.github.sbkimxtheia.metaitems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Optional;

public final class MetaItems extends JavaPlugin {
	
	private static final String prefix = ChatColor.AQUA + "[MetaItems]";
	public static MetaItems plugin;
	public static ConsoleCommandSender logger;
	
	@Override
	public void onEnable () {
		plugin = this;
		logger = Bukkit.getServer().getConsoleSender();
		
		Log("Hello, World!");
		
		File pluginPath = getDataFolder();
		if (! pluginPath.exists()) pluginPath.mkdir();
		File presetPath = new File(pluginPath, "presets");
		if (! presetPath.exists()) presetPath.mkdir();
		
		
		Log(pluginPath.toString());
		Log(presetPath.toString());
		
		File[] files = presetPath.listFiles();
//		if (files != null && files.length == 0) {
//			getResource("preset-example.yml").re
//		}
		for (File file : files) {
			Log(file.getAbsolutePath());
			
			YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
			
			String codeName = configuration.getString(ItemLoader.CODENAME);
			if (codeName == null) {
				Log("CodeName Null");
				continue;
			}
			
			Optional<MetaItem> itemOptional = ItemManager.createNew(codeName);
			if (! itemOptional.isPresent()) {
				Log("CodeName Duplicates");
				continue;
			}
			MetaItem item = itemOptional.get();
			
			
			item.setMaterial(Material.getMaterial(configuration.getString(ItemLoader.MATERIAL, "GOLDEN_SWORD")));
			item.setDisplayName(configuration.getString(ItemLoader.DISPLAYNAME, "Empty Item Display Name"));
			item.setItemDamage(configuration.getInt(ItemLoader.ITEMDAMAGE, 0));
			item.setUnbreakable(configuration.getBoolean(ItemLoader.Unbreakable, false));
			item.setWieldDamageBase(configuration.getDouble(ItemLoader.WieldDamageBase, 5.0));
			
			
			getServer().getPlayer("SBkimXTHEIA").getInventory().addItem(item.toItemStack(1));
		}
	}
	
	@Override
	public void onDisable () {
		// Plugin shutdown logic
	}
	
	@Override
	public void onLoad () {
	
	}
	
	public static void Log (String... message) {
		for (String s : message) {
			String _msg_ = prefix + ChatColor.WHITE + " " + s;
			logger.sendMessage(_msg_);
		}
	}
	
	public static void Message (CommandSender player, String... message) {
		for (String s : message) {
			String _msg_ = prefix + ChatColor.WHITE + " " + s;
			player.sendMessage(message);
		}
	}
}
