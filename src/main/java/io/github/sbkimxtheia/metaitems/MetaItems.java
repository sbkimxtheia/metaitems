package io.github.sbkimxtheia.metaitems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.List;
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
		if (! presetPath.exists()){
			presetPath.mkdir();
		}
		File example = new File(presetPath, "preset-example.yml");
		if(!example.exists()) {
			Log("Trying to Create example preset file...");
			try {
				example.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				InputStream stream = getResource("preset-example.yml");
				OutputStream outputStream = new FileOutputStream(example);
				while(true){
					int i = stream.read();
					if(i == -1){
						break;
					}
					else{
						outputStream.write(i);
					}
				}
				outputStream.close();
				stream.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File[] files = presetPath.listFiles();
//		if (files != null && files.length == 0) {
//			getResource("preset-example.yml").re
//		}
		for (File file : files) {
			Log("","Found: " + file.getAbsolutePath());
			if(!file.getName().endsWith(".yml")){
				Log(ChatColor.RED + "Skipped " + file.getName() + ": Filename not ends with \".yml\"");
				continue;
			}
			YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
			
			String codeName = configuration.getString(ItemLoader.CODENAME);
			if (codeName == null) {
				Log(ChatColor.RED + "Skipped " + file.getName() + ": Entry \"CodeName\" is null");

				continue;
			}
			
			Optional<MetaItem> itemOptional = ItemManager.createNew(codeName);
			if (! itemOptional.isPresent()) {
				Log(ChatColor.RED + "Skipped " + file.getName() + ": Codename \"" + codeName + "\" Already exists" );
				
				continue;
			}
			MetaItem item = itemOptional.get();
			
			
			item.setMaterial(Material.getMaterial(configuration.getString(ItemLoader.MATERIAL, "GOLDEN_SWORD")));
			item.setDisplayName(configuration.getString(ItemLoader.DISPLAYNAME, "Empty Item Display Name"));
			item.setItemDamage(configuration.getInt(ItemLoader.ITEMDAMAGE, 0));
			item.setUnbreakable(configuration.getBoolean(ItemLoader.Unbreakable, false));
			item.setWieldDamageBase(configuration.getDouble(ItemLoader.WieldDamageBase, 5.0));
			List<String> lores = configuration.getStringList(ItemLoader.Lores);
			for (String lore : lores) {
				item.addLores(lore);
			}
			List<String> attrs = configuration.getStringList(ItemLoader.AttributeModifiers);
			for (String attr : attrs) {
				String[] attrToken = attr.split("/");
				if(attrToken.length != 4){
					Log(ChatColor.RED + "Cannot Parse AttributeModifier [ " + ChatColor.WHITE + attr + ChatColor.RED + " ]!" );
					continue;
				}
				String _slotStr_ = attrToken[0];
				EquipmentSlot slot = _slotStr_.equals("ALL")? null : EquipmentSlot.valueOf(_slotStr_);
				Attribute attribute = Attribute.valueOf(attrToken[1]);
				AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(attrToken[2]);
				double value = Double.parseDouble(attrToken[3]);
				item.addAttrModificationList(new AttrModification(attribute, operation,value,slot));
			}
			
			Log(
					"Loaded " + ChatColor.GREEN + item.codeName + ChatColor.WHITE + "! " +
					ChatColor.GRAY + "(" + file.getPath() + ")", "");
			Optional.ofNullable(getServer().getPlayer("SBkimXTHEIA")).ifPresent(p -> p.getInventory().addItem(item.toItemStack(1)));
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
			String _msg_ = prefix + ChatColor.WHITE + " " + s + ChatColor.WHITE;
			player.sendMessage(message);
		}
	}
}
