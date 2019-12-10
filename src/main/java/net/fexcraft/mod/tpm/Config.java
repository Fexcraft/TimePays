package net.fexcraft.mod.tpm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {
	
	public static long INTERVAL;
	public static boolean D;
	//
	public static File CONFIG_PATH;
	public static final String CATEGORY = "Settings";
	private static Configuration config;
	
	public static void initialize(FMLPreInitializationEvent event){
		CONFIG_PATH = event.getSuggestedConfigurationFile().getParentFile();
		config = new Configuration(event.getSuggestedConfigurationFile(), "1.0", true);
		config.load();
		config.setCategoryRequiresMcRestart(CATEGORY, true);
		config.setCategoryRequiresWorldRestart(CATEGORY, true);
		config.setCategoryComment(CATEGORY, "General State Settings.");
		refresh();
		config.save();
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