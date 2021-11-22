package io.github.sbkimxtheia.metaitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MetaItem implements Serializable {
	private static final NamespacedKey KEY_METAUID = new NamespacedKey(MetaItems.plugin, "METAITEMS_METAUID");
	private static final NamespacedKey KEY_CODENAME = new NamespacedKey(MetaItems.plugin, "METAITEMS_CODENAME");
	
	//-Required-//
	
	// Core
	final String codeName;
	final int metaUid;
	
	// Required
	String displayName = ChatColor.YELLOW + "new MetaItem";
	Material material = Material.GOLDEN_SWORD;
	
	//-Optionals-//
	
	// Basic
	int itemDamage = 250;
	boolean unbreakable = false;
	int customModelData = - 2;
	
	// Misc
	ArrayList<String> lores = new ArrayList<>();
	
	// Combat
	double wieldDamageBase = 5;
	double wieldDamageCritAdditional = 3;
	float wieldCritProb = 0.1f;
	ArrayList<AttrModification> attrModificationList = new ArrayList<>();
	
	
	// Getter & Setter
	public String getDisplayName () {
		return displayName;
	}
	
	public MetaItem setDisplayName (String displayName) {
		this.displayName = displayName;
		return this;
	}
	
	public Material getMaterial () {
		return material;
	}
	
	public MetaItem setMaterial (Material material) {
		this.material = material;
		return this;
	}
	
	public int getItemDamage () {
		return itemDamage;
	}
	
	public MetaItem setItemDamage (int itemDamage) {
		this.itemDamage = itemDamage;
		return this;
	}
	
	public boolean isUnbreakable () {
		return unbreakable;
	}
	
	public MetaItem setUnbreakable (boolean unbreakable) {
		this.unbreakable = unbreakable;
		return this;
	}
	
	public int getCustomModelData () {
		return customModelData;
	}
	
	public MetaItem setCustomModelData (int customModelData) {
		this.customModelData = customModelData;
		return this;
	}
	
	public ArrayList<String> getLores () {
		return lores;
	}
	
	public MetaItem addLores (String... args) {
		this.lores.addAll(Arrays.asList(args));
		return this;
	}
	
	public double getWieldDamageBase () {
		return wieldDamageBase;
	}
	
	public MetaItem setWieldDamageBase (double wieldDamageBase) {
		this.wieldDamageBase = wieldDamageBase;
		return this;
	}
	
	public double getWieldDamageCritAdditional () {
		return wieldDamageCritAdditional;
	}
	
	public MetaItem setWieldDamageCritAdditional (double wieldDamageCritAdditional) {
		this.wieldDamageCritAdditional = wieldDamageCritAdditional;
		return this;
	}
	
	public float getWieldCritProb () {
		return wieldCritProb;
	}
	
	public MetaItem setWieldCritProb (float wieldCritProb) {
		this.wieldCritProb = wieldCritProb;
		return this;
	}
	
	public ArrayList<AttrModification> getAttrModificationList () {
		return attrModificationList;
	}
	
	public MetaItem addAttrModificationList (AttrModification... modifications) {
		this.attrModificationList.addAll(Arrays.asList(modifications));
		return this;
	}
	
	public MetaItem (String codeName, int uid) {
		this.codeName = codeName;
		this.metaUid = uid;
		displayName = codeName;
	}
	
	
	public ItemStack toItemStack (int amount) {
		ItemStack item = new ItemStack(this.material, amount);
		ItemMeta meta = MetaItems.plugin.getServer().getItemFactory().getItemMeta(material);
		meta.getPersistentDataContainer().set(KEY_METAUID, PersistentDataType.INTEGER, metaUid);
		meta.getPersistentDataContainer().set(KEY_CODENAME, PersistentDataType.STRING, codeName);
		meta.setDisplayName(displayName);
		meta.setLore(lores);
		meta.setUnbreakable(unbreakable);
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
				new AttributeModifier(
						new UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong()),
						"METAITEMS_WIELDDAMAGEBASE",
						wieldDamageBase,
						AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
		int _attrC_ = 0;
		for (AttrModification attrModification : attrModificationList) {
			if (attrModification.slot.isPresent()) {
				meta.addAttributeModifier(
						attrModification.targetAttr, new AttributeModifier(new UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong()),
								"METAITEMS_ATTR_" + _attrC_, attrModification.value, attrModification.operation, attrModification.slot.get()));
				
			}
			else {
				meta.addAttributeModifier(
						attrModification.targetAttr, new AttributeModifier(new UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong()),
								"METAITEMS_ATTR_" + _attrC_, attrModification.value, attrModification.operation));
			}
			
			
		}
		if (customModelData != - 2) meta.setCustomModelData(customModelData);
		if (meta instanceof Damageable) ((Damageable) meta).setDamage(itemDamage);
		item.setAmount(amount);
		
		
		item.setItemMeta(meta);
		return item;
	}
	
}

