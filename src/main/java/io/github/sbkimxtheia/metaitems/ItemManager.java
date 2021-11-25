package io.github.sbkimxtheia.metaitems;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ItemManager {
	// "MAIN" HashMap
	public static HashMap<Integer,MetaItem> metaUid_to_metaItem = new HashMap<>();
	public static HashMap<String,MetaItem> codeName_to_metaItem = new HashMap<>();
	
	public static Optional<MetaItem> createNew(String codeName){
		String __codeName__= codeName.replace(' ', '_');
		
		if(codeName_to_metaItem.containsKey(__codeName__)) return Optional.empty();
		
		int uid;
		while(true){
			int _tmp = ThreadLocalRandom.current().nextInt(- 999999, -99999);
			if(!metaUid_to_metaItem.containsKey(_tmp)){
				uid = _tmp;
				break;
			}
		}
		
		MetaItem newItem = new MetaItem(__codeName__, uid);
		metaUid_to_metaItem.put(uid, newItem);
		codeName_to_metaItem.put(__codeName__, newItem);
		MetaItems.Log("new MetaItem Created!","CodeName: " + newItem.codeName,"META UID: " + newItem.metaUid);
		return Optional.of(newItem);
	}
	
	public static boolean register(MetaItem item){
		
	}
	
}
