package net.fexcraft.mod.tpm;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.tpm.cap.CapabilityContainer;
import net.fexcraft.mod.tpm.cap.PlayerCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

@Mod.EventBusSubscriber
public class Tracker extends TimerTask {
	
	private static long lastinterval = 0, passed = 0;

	@Override
	public void run(){
		passed = Time.getDate() - lastinterval;
		List<EntityPlayerMP> players = Static.getServer().getPlayerList().getPlayers();
		for(EntityPlayer player : players){
			player.getCapability(CapabilityContainer.PLAYER, null).onInterval(lastinterval, passed);
		}
		lastinterval = Time.getDate();
	}
	
	@SubscribeEvent
	public static void onLogin(PlayerLoggedInEvent event){
		PlayerCapability player = event.player.getCapability(CapabilityContainer.PLAYER, null);
		if(player == null){ Print.chat(event.player, "Player data couldn't be loaded."); return; }
		player.read(loadPlayerData(event.player));
	}

	@SubscribeEvent
	public static void onLogout(PlayerLoggedOutEvent event){
		PlayerCapability player = event.player.getCapability(CapabilityContainer.PLAYER, null);
		if(player == null){ Print.chat(event.player, "Player data couldn't be loaded."); return; }
		savePlayerData(event.player, player.write());
	}
	
	private static NBTTagCompound loadPlayerData(EntityPlayer player){
		File file = new File(player.world.getSaveHandler().getWorldDirectory(), "/timepays/players/" + player.getGameProfile().getId().toString() + ".nbt");
		if(file.exists()){
			try{
				return CompressedStreamTools.read(file);
			}
			catch(IOException e){
				Print.chat(player, "&cYour TPM save file failed to load - report this an an admin. &6LOG IN CONSOLE");
				e.printStackTrace(); return null;
			}
		}
		else return new NBTTagCompound();
	}
	
	private static void savePlayerData(EntityPlayer player, NBTTagCompound compound){
		File file = new File(player.world.getSaveHandler().getWorldDirectory(), "/timepays/players/" + player.getGameProfile().getId().toString() + ".nbt");
		if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
		try{ CompressedStreamTools.write(compound, file); } catch(Exception e){ e.printStackTrace(); }
	}

	@SubscribeEvent
	public static void onRespawn(PlayerEvent.Clone event){
		event.getEntityPlayer().getCapability(CapabilityContainer.PLAYER, null).copyFrom(event.getOriginal().getCapability(CapabilityContainer.PLAYER, null));
	}
	
	@SubscribeEvent
	public static void onAttach(AttachCapabilitiesEvent<Entity> event){
    	if(event.getObject() instanceof EntityPlayerMP){
    		event.addCapability(PlayerCapability.REGISTRY_NAME, new CapabilityContainer((EntityPlayer)event.getObject()));
    	}
	}

}
