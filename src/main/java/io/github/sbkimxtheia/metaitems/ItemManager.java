package io.github.sbkimxtheia.metaitems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ItemManager {
	
	public static final NamespacedKey KEY_METAUID = new NamespacedKey(MetaItems.plugin, "METAITEMS_METAUID");
	public static final NamespacedKey KEY_CODENAME = new NamespacedKey(MetaItems.plugin, "METAITEMS_CODENAME");
	
	// "MAIN" HashMap
	public static HashMap<Integer, MetaItem> metaUid_to_metaItem = new HashMap<>();
	public static HashMap<String, MetaItem> codeName_to_metaItem = new HashMap<>();
	
	
	public static boolean codenameAvailable(String codeName) {
		return ! codeName_to_metaItem.containsKey(codeName);
	}
	
	public static void register(MetaItem item) {
		metaUid_to_metaItem.put(item.metaUid, item);
		codeName_to_metaItem.put(item.codeName, item);
	}
	
	public static int newUid() {
		int uid;
		while (true) {
			int _tmp = ThreadLocalRandom.current().nextInt(- 999999, - 99999);
			if (! metaUid_to_metaItem.containsKey(_tmp)) {
				uid = _tmp;
				break;
			}
		}
		return uid;
	}
	
	public static Optional<MetaItem> isMetaItem(ItemStack item) {
		
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			Integer uid = meta.getPersistentDataContainer().get(KEY_METAUID, PersistentDataType.INTEGER);
			if (uid != null) {
				return Optional.ofNullable(metaUid_to_metaItem.get(uid));
			}
		}
		return Optional.empty();
	}
	
}
