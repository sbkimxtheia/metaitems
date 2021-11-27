package io.github.sbkimxtheia.metaitems;

import org.bukkit.enchantments.Enchantment;

public class Enchant {
	final Enchantment enchantment;
	final int level;
	final boolean ignoreRestriction;
	
	public Enchant(Enchantment enchantment, int level, boolean ignoreRestriction) {
		this.enchantment = enchantment;
		this.level = level;
		this.ignoreRestriction = ignoreRestriction;
	}
}
