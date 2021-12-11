package io.github.sbkimxtheia.metaitems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ItemManager {
	
	public static final NamespacedKey KEY_METAUID = new NamespacedKey(MetaItems.plugin, "METAITEMS_METAUID");
	public static final NamespacedKey KEY_CODENAME = new NamespacedKey(MetaItems.plugin, "METAITEMS_CODENAME");
	
	// "MAIN" HashMap
	public static HashMap<Integer, MetaItem> items = new HashMap<>();
	
	
	public static boolean codenameAvailable(String codeName) {
		if(codeName == null || codeName.isEmpty() || !(codeName.trim().length() > 0)){
			return false;
		}
		final Collection<MetaItem> _items_ = items.values();
		for (MetaItem _item_ : _items_) {
			if (_item_.codeName.equals(codeName)) {
				return false;
			}
		}
		return true;
	}
	
	public static void register(MetaItem item) {
		items.put(item.metaUid, item);
	}
	
	public static int newUid() {
		int uid;
		while (true) {
			int _tmp = ThreadLocalRandom.current().nextInt(- 999999, - 99999);
			if (! items.containsKey(_tmp)) {
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
				return Optional.ofNullable(items.get(uid));
			}
		}
		return Optional.empty();
	}
	
}
