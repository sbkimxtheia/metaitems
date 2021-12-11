package io.github.sbkimxtheia.metaitems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class MetaItems extends JavaPlugin {
	
	private static final String prefix = ChatColor.AQUA + "[MetaItems]";
	public static MetaItems plugin;
	public static ConsoleCommandSender logger;
	
	@Override
	public void onEnable() {
		plugin = this;
		logger = Bukkit.getServer().getConsoleSender();
		getServer().getPluginManager().registerEvents(new EventListener(), plugin);
		
		Log("Hello, World!");
		
		File pluginPath = getDataFolder();
		if (! pluginPath.exists()) pluginPath.mkdir();
		File presetPath = new File(pluginPath, "presets");
		if (! presetPath.exists()) {
			presetPath.mkdir();
		}
		File example = new File(presetPath, "preset-example.yml");
		if (! example.exists()) {
			Log("Trying to Create example preset file...");
			try {
				example.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				InputStream stream = getResource("preset-example.yml");
				OutputStream outputStream = new FileOutputStream(example);
				while (true) {
					int i = stream.read();
					if (i == - 1) {
						break;
					}
					else {
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
			Log("", "Found new Preset File! " + ChatColor.GRAY + "(" + file.getAbsolutePath() + ")");
			if (! file.getName().endsWith(".yml")) {
				Log(ChatColor.RED + "Skipped " + file.getName() + ": Filename not ends with \".yml\"");
				continue;
			}
			YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
			
			String codeName = configuration.getString(ItemLoader.CODENAME);
			if (codeName == null || codeName.isEmpty()) {
				Log(ChatColor.RED + "Skipped " + file.getName() + ": Entry \"CodeName\" is null or empty");
				
				continue;
			}
			
			if (! ItemManager.codenameAvailable(codeName)) {
				Log(ChatColor.RED + "Skipped " + file.getName() + ": Codename \"" + codeName + "\" Already exists or Invalid!");
				continue;
			}
			
			MetaItem item = new MetaItem(codeName, ItemManager.newUid());
			Log("Started Loading new Item " + ChatColor.AQUA + item.codeName + ChatColor.WHITE + "!");
			
			ArrayList<String> errors = new ArrayList<>();
			
			item.setDisplayName(configuration.getString(ItemLoader.DISPLAYNAME, "Empty Item Display Name"));
			item.setUnbreakable(configuration.getBoolean(ItemLoader.Unbreakable, false));
			item.setCanEnchantTable(configuration.getBoolean(ItemLoader.AllowEnchantTable, false));
			
			// Material
			String _matStr_ = configuration.getString(ItemLoader.MATERIAL, "GOLDEN_SWORD");
			Material _material_ = Material.getMaterial(_matStr_);
			if (_material_ == null) {
				errors.add(ChatColor.RED + "[ " + ChatColor.WHITE + _matStr_ + ChatColor.RED + " ] is not available Material!");
			}
			else {
				item.setMaterial(_material_);
			}
			
			// Durability Damage
			int damage = configuration.getInt(ItemLoader.ITEMDAMAGE, 0);
			if (damage < 0) {
				errors.add(ChatColor.RED + "Item Damage must be >= 0! (Input: " + ChatColor.WHITE + damage + ChatColor.RED + ")");
			}
			else {
				item.setItemDamage(damage);
			}
			
			// Wield Damage
			double wieldDmgBase = configuration.getDouble(ItemLoader.WieldDamageBase, 5.0);
			if (wieldDmgBase < 0) {
				errors.add(ChatColor.RED + "Item Damage must be >= 0! (Input: " + ChatColor.WHITE + wieldDmgBase + ChatColor.RED + ")");
			}
			else {
				item.setWieldDamageBase(wieldDmgBase);
			}
			
			// Lores
			List<String> loresStr = configuration.getStringList(ItemLoader.Lores);
			for (String lore : loresStr) {
				item.addLores(lore);
			}
			
			// Attrs
			List<String> attrsStr = configuration.getStringList(ItemLoader.AttributeModifiers);
			for (String attrStr : attrsStr) {
				String[] attrToken = attrStr.split("/");
				if (attrToken.length != 4) {
					errors.add(ChatColor.RED + "Cannot Parse AttributeModifier [ " + ChatColor.WHITE + attrStr + ChatColor.RED + " ]!");
					continue;
				}
				String _slotStr_ = attrToken[0];
				EquipmentSlot slot = _slotStr_.equals("ALL") ? null : EquipmentSlot.valueOf(_slotStr_);
				Attribute attribute = Attribute.valueOf(attrToken[1]);
				AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(attrToken[2]);
				double value = Double.parseDouble(attrToken[3]);
				item.addAttrModificationList(new AttrModification(attribute, operation, value, slot));
			}
			
			// Flags
			List<String> flagsStr = configuration.getStringList(ItemLoader.Flags);
			for (String flagStr : flagsStr) {
				ItemFlag flag = ItemFlag.valueOf(flagStr);
				item.addFlag(flag);
			}
			
			// Enchants
			List<String> enchantsStr = configuration.getStringList(ItemLoader.Enchants);
			for (String enchantStr : enchantsStr) {
				
				final String errMsg = ChatColor.RED + "Cannot Parse Enchant [ " + ChatColor.WHITE + enchantStr + ChatColor.RED + " ] :: ";
				
				String[] enchantToken = enchantStr.split("/");
				
				if (enchantToken.length != 3) {
					errors.add(errMsg + ChatColor.RED + "Modifier must follow format like [ " + ChatColor.WHITE + "{ENCHANTMENT}/{VALUE}/{IGNORELEVELRESTRICTION}" + ChatColor.RED + " ]!");
					continue;
				}
				
				String _enchantmentToken_ = enchantToken[0].toLowerCase(Locale.ROOT);
				Enchantment _enchantment_ = Enchantment.getByKey(NamespacedKey.minecraft(_enchantmentToken_));
				if (_enchantment_ == null) {
					errors.add(errMsg + ChatColor.RED + "Cannot find Enchantment [ " + ChatColor.WHITE + _enchantmentToken_ + ChatColor.RED + " ]!");
					continue;
				}
				
				try {
					String _levelStr_ = enchantToken[1];
					
					int _level_ = Integer.parseInt(_levelStr_);
					boolean ignore = enchantToken[2].toLowerCase(Locale.ROOT).equals("true");
					item.addEnchant(new Enchant(_enchantment_, _level_, ignore));
					
				} catch (NumberFormatException e) {
					errors.add(errMsg + ChatColor.RED + "[ " + ChatColor.WHITE + enchantToken[1] + ChatColor.RED + " ] is not a Integer!");
					continue;
				} catch (Exception e) {
					errors.add(errMsg + ChatColor.RED + e.getMessage());
					continue;
				}
			}
			
			if (errors.isEmpty()) {
				ItemManager.register(item);
				Log("Successfully Loaded " + ChatColor.AQUA + item.codeName + ChatColor.WHITE + "! " +
						ChatColor.GRAY + "(" + file.getPath() + ")");
				Optional.ofNullable(getServer().getPlayer("SBkimXTHEIA")).ifPresent(p -> p.getInventory().addItem(item.toItemStack(1)));
			}
			else {
				Log(ChatColor.RED + "Failed to Load " + ChatColor.AQUA + item.codeName + ChatColor.GRAY + " (" + file.getPath() + ")" + ChatColor.WHITE + " :: ");
				errors.forEach(MetaItems::Log);
			}

			
			
		}
		Log("", ChatColor.YELLOW + "Finished indexing every single preset files!", "");
	}
	
	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
	
	@Override
	public void onLoad() {
	
	}
	
	public static void Log(String... message) {
		for (String s : message) {
			String _msg_ = prefix + ChatColor.WHITE + " " + s;
			logger.sendMessage(_msg_);
		}
	}
	
	public static void Message(CommandSender player, String... message) {
		for (String s : message) {
			String _msg_ = prefix + ChatColor.WHITE + " " + s + ChatColor.WHITE;
			player.sendMessage(message);
		}
	}
}
