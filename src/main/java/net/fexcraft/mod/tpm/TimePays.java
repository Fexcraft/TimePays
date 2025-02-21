package net.fexcraft.mod.tpm;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Timer;

import net.fexcraft.lib.common.math.Time;
import net.fexcraft.mod.tpm.compat.MCCmdHandler;
import net.fexcraft.mod.tpm.compat.MCItemHandler;
import net.fexcraft.mod.tpm.compat.RewardHandler;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mod(modid = TimePays.MODID, name = "Time Pays Mod", version = TimePays.VERSION, dependencies = "required-after:fcl;after:fsmm",
	guiFactory = "net.fexcraft.mod.tp.GuiFactory", acceptedMinecraftVersions = "*", acceptableRemoteVersions = "*")
public class TimePays {

	public static final String VERSION = "2.0";
	public static final String MODID = "timepays";
	public static final String ADMIN_PERM = "timepays.admin";
	//
	@Mod.Instance(MODID)
	public static TimePays INSTANCE;
	public static Timer INTERVAL;
	public static Config CONFIG;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		CONFIG = new Config(new File(event.getSuggestedConfigurationFile().getParentFile() + "/tpm.json"));
		TpmGroups.init(new File(event.getSuggestedConfigurationFile().getParentFile() + "/tpm_groups.json"));
		UniEntity.register(new TpmData(null));
	}
	
	@Mod.EventHandler
	public void properInit(FMLInitializationEvent event){
		PermissionAPI.registerNode(ADMIN_PERM, DefaultPermissionLevel.OP, "TimePays Admin Perm");
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){
		if(Loader.isModLoaded("fsmm")){
			RewardHandler.HANDLERS.put("fsmm:item", new net.fexcraft.mod.tpm.compat.FSMMHandler.Item());
			RewardHandler.HANDLERS.put("fsmm:currency", new net.fexcraft.mod.tpm.compat.FSMMHandler.Currency());
		}
		RewardHandler.HANDLERS.put("item", new MCItemHandler());
		RewardHandler.HANDLERS.put("command", new MCCmdHandler());
	}
	
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		event.registerServerCommand(new TpmCommand());
	}
	
	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event){
		if(INTERVAL == null){
			LocalDateTime midnight = LocalDateTime.of(LocalDate.now(ZoneOffset.systemDefault()), LocalTime.MIDNIGHT);
			long mid = midnight.toInstant(ZoneOffset.UTC).toEpochMilli(); long date = Time.getDate();
			while((mid += Config.INTERVAL) < date);
			(INTERVAL = new Timer()).schedule(new Tracker(), new Date(mid), EnvInfo.DEV ? 10000 : Config.INTERVAL * Time.MIN_MS);
		}
		//Tracker.load();
	}
	
	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event){
		//Tracker.save();
		INTERVAL.cancel();
		INTERVAL = null;
	}

}
