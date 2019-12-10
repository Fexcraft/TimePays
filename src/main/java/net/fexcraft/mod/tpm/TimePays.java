package net.fexcraft.mod.tpm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Timer;

import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.mc.network.SimpleUpdateHandler;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.tpm.cap.CapabilityContainer;
import net.fexcraft.mod.tpm.cap.PlayerCapability;
import net.fexcraft.mod.tpm.compat.MCItemHandler;
import net.fexcraft.mod.tpm.compat.RewardHandler;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = TimePays.MODID, name = "Time Pays Mod", version = TimePays.VERSION, dependencies = "required-after:fcl;after:fsmm", serverSideOnly = true,
	guiFactory = "net.fexcraft.mod.tp.GuiFactory", acceptedMinecraftVersions = "*", acceptableRemoteVersions = "*")
public class TimePays {
	
	public static final String VERSION = "@VERSION@";
	public static final String MODID = "timepays";
	public static final String ADMIN_PERM = "timepays.admin";
	public static final String PREFIX = "&0[&1TPM&0]";
	//
	@Mod.Instance(MODID)
	public static TimePays INSTANCE;
	public static Timer INTERVAL;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Config.initialize(event);
    	CapabilityManager.INSTANCE.register(PlayerCapability.class,
    		new CapabilityContainer.Storage(), new CapabilityContainer.Callable());
	}
	
	@Mod.EventHandler
	public void properInit(FMLInitializationEvent event){
		//
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){
		if(Loader.isModLoaded("fsmm")){
			RewardHandler.HANDLERS.put("fsmm_item", new net.fexcraft.mod.tpm.compat.FSMMHandler.Item());
			RewardHandler.HANDLERS.put("fsmm_currency", new net.fexcraft.mod.tpm.compat.FSMMHandler.Currency());
		}
		RewardHandler.HANDLERS.put("item", new MCItemHandler());
		//
		SimpleUpdateHandler.register(MODID, 1, VERSION);
		SimpleUpdateHandler.setUpdateMessage(MODID, PREFIX + " &7New Version available! &0(&8" + SimpleUpdateHandler.getLatestVersionOf(MODID) + "&0)");
	}
	
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		//
	}
	
	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event){
		if(INTERVAL == null){
			LocalDateTime midnight = LocalDateTime.of(LocalDate.now(ZoneOffset.systemDefault()), LocalTime.MIDNIGHT);
			long mid = midnight.toInstant(ZoneOffset.UTC).toEpochMilli(); long date = Time.getDate();
			while((mid += Config.INTERVAL) < date);
			(INTERVAL = new Timer()).schedule(new Tracker(), new Date(mid), Static.dev() ? 10000 : Config.INTERVAL * 1000);
		}
		//Tracker.load();
	}
	
	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event){
		//Tracker.save();
	}

}
