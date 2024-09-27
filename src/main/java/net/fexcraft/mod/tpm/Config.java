package net.fexcraft.mod.tpm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.lib.common.json.JsonUtil;
import net.fexcraft.mod.tpm.compat.RewardHandler;
import net.fexcraft.mod.uni.ConfigBase;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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