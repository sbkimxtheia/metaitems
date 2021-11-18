package io.github.sbkimxtheia.metaitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MetaItem {
	
	//-Required-//
	
	// Core
	final String codeName;
	final int metaUid;
	
	// Required
	String displayName = ChatColor.YELLOW + "new MetaItem";
	Material material = Material.GOLDEN_SWORD;
	
	//-Optionals-//
	
	// Basic
	boolean unbreakable = false;
	int customModelData;
	
	// Misc
	List<String> lores;
	
	// Combat
	double wieldDamageBase;
	double wieldDamageCritAdditional;
	float wieldCritProb;
	
	
	public MetaItem(String codeName, int uid){
		this.codeName = codeName;
		this.metaUid = uid;
		displayName = codeName;
	}
	
	
	
	public ItemStack toItemStack(int amount){
		ItemStack item = new ItemStack(this.material, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lores);
		meta.setUnbreakable(unbreakable);
		
		return item;
	}
	
}
