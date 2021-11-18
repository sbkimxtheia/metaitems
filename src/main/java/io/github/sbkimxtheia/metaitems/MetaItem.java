package io.github.sbkimxtheia.metaitems;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MetaItem {
	
	// Core
	int metaUid;
	String codeName;
	
	// Required
	String displayName;
	Material material = Material.
	
	// Optionals
	// Basic
	boolean unbreakable = false;
	int customModelData;
	// Misc
	List<String> lores;
	
	// Combat
	double wieldDamageBase;
	double wieldDamageCritAdditional;
	float wieldCritProb;
	
	public ItemStack toItemStack(int amount){
		ItemStack item = new ItemStack(this.material, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lores);
		meta.setUnbreakable(unbreakable);
		
		return item;
	}
	
}
