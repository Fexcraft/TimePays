package net.fexcraft.mod.tpm;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TpmGroups {

	public static LinkedHashMap<String, ArrayList<UUID>> GROUPS = new LinkedHashMap();
	public static File FILE;

	public static void init(File file){
		FILE = file;
		JsonMap map = JsonHandler.parse(file).asMap();
		for(Map.Entry<String, JsonValue<?>> entry : map.entries()){
			ArrayList<UUID> list = new ArrayList<>();
			for(JsonValue<?> val : entry.getValue().asArray().value){
				try{
					list.add(UUID.fromString(val.string_value()));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			GROUPS.put(entry.getKey(), list);
		}
	}

	public static void save(){
		JsonMap map = new JsonMap();
		for(Map.Entry<String, ArrayList<UUID>> entry : GROUPS.entrySet()){
			JsonArray array = new JsonArray();
			for(UUID uuid : entry.getValue()){
				array.add(uuid.toString());
			}
			map.add(entry.getKey(), array);
		}
		JsonHandler.print(FILE, map, JsonHandler.PrintOption.DEFAULT);
	}

	public static boolean isInGroup(UUID uuid, String group){
		if(GROUPS.containsKey(group)){
			return GROUPS.get(group).contains(uuid);
		}
		return false;
	}

	public static boolean isInAnyGroup(UUID uuid, ArrayList<String> groups){
		for(String group : groups) if(isInGroup(uuid, group)) return true;
		return false;
	}

}
