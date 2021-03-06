package net.fexcraft.mod.tpm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.fexcraft.lib.common.json.JsonUtil;
import net.fexcraft.mod.tpm.compat.RewardHandler;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {
	
	public static long INTERVAL;
	//
	public static final String CATEGORY = "Settings";
	private static Configuration config;
	
	public static void initialize(FMLPreInitializationEvent event){
		File file = new File(event.getSuggestedConfigurationFile().getParentFile() + "/tpm_rewards.json");
		if(!file.getParentFile().exists()) file.getParentFile().mkdirs(); if(!file.exists()) JsonUtil.write(file, new JsonArray());
		JsonElement arrey = JsonUtil.read(file, false); JsonArray array = arrey.getAsJsonArray();
		for(JsonElement elm : array){
			try{
				Reward reward = new Reward(elm.getAsJsonObject());
				RewardHandler.REWARDS.put(reward.id, reward);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		//
		config = new Configuration(event.getSuggestedConfigurationFile(), "1.0", true);
		config.load();
		config.setCategoryRequiresMcRestart(CATEGORY, true);
		config.setCategoryRequiresWorldRestart(CATEGORY, true);
		config.setCategoryComment(CATEGORY, "General Settings.");
		refresh(); config.save();
	}

	public static List<IConfigElement> getList(){
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.add(new ConfigElement(Config.getConfig().getCategory(CATEGORY)));
		return list;
	}

	public static final Configuration getConfig(){
		return config;
	}
	
	private static void refresh(){
		INTERVAL = config.getInt("interval", CATEGORY, 1, 1, 1440, "Interval in which player activity is checked, in minutes.");
	}
	
}