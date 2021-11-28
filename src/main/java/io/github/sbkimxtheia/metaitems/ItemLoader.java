package io.github.sbkimxtheia.metaitems;

import java.util.HashMap;

public class ItemLoader {
	public static HashMap<String, String> setterMap = new HashMap<>();
	
	public static ParsedMetaItemSetter wrapString (MetaItemSetter<String> setter) {
		return (item, val) -> {
			setter.Set(item, val);
			return true;
		};
	}
	
	public static ParsedMetaItemSetter wrapInteger (MetaItemSetter<Integer> setter) {
		return (item, str) -> {
			try {
				int value = Integer.parseInt(str);
				setter.Set(item, value);
				return true;
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			return false;
		};
	}
	
	public static final String
			CODENAME = "CodeName",
			MATERIAL = "Material",
			DISPLAYNAME = "DisplayName",
			Lores = "Lores",
			ITEMDAMAGE = "ItemDurabilityDamage",
			Unbreakable = "Unbreakable",
			WieldDamageBase = "WieldDamageBase",
			WieldDamageCrit = "WieldDamageCrit",
			WieldProbCrit = "WieldProbCrit",
			AttributeModifiers = "AttributeModifiers",
			CustomModelData = "CustomModelData",
			Flags = "Flags",
			Enchants = "Enchants",
			AllowEnchantTable = "AllowEnchantTable";
	
}

