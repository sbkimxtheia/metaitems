package io.github.sbkimxtheia.metaitems;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ItemManager {
	// "MAIN" HashMap
	public static HashMap<Integer, MetaItem> metaUid_to_metaItem = new HashMap<>();
	public static HashMap<String, MetaItem> codeName_to_metaItem = new HashMap<>();
	
	
	public static boolean codenameAvailable(String codeName) {
		return !codeName_to_metaItem.containsKey(codeName);
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
	
}
