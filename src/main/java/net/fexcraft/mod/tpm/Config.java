package net.fexcraft.mod.tpm;

import java.io.File;
import java.util.Map;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.mod.tpm.compat.RewardHandler;
import net.fexcraft.mod.uni.ConfigBase;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Config extends ConfigBase {
	
	public static long INTERVAL;

	public Config(File fl){
		super(fl);
	}

	@Override
	protected void fillInfo(JsonMap map){
		map.add("info", "TimePays Configuration File");
		map.add("wiki", "https://fexcraft.net/wiki/mod/timepays");
	}

	@Override
	protected void fillEntries(){
		entries.add(new ConfigEntry(this, "general", "interval", 1).rang(1, 1440)
			.info("Interval in which player activity is checked, in minutes.")
			.cons((con, map) -> INTERVAL = con.getInteger(map))
		);
		entries.add(new ConfigEntry(this, "general", "rewards", new JsonMap())
			.info("Map with Reward entries.")
			.cons((con, map) -> {
				RewardHandler.REWARDS.clear();
				JsonMap rwm = con.getJson(map).asMap();
				for(Map.Entry<String, JsonValue<?>> entry : rwm.entries()){
					try{
						Reward reward = new Reward(entry.getKey(), entry.getValue().asMap());
						RewardHandler.REWARDS.put(reward.id, reward);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			})
		);
	}

	@Override
	protected void onReload(JsonMap map){

	}

}