package net.fexcraft.mod.tpm;

import java.util.List;
import java.util.TimerTask;

import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Tracker extends TimerTask {

	private static long lastinterval = 0, passed = 0;

	@Override
	public void run(){
		passed = Time.getDate() - lastinterval;
		List<EntityPlayerMP> players = Static.getServer().getPlayerList().getPlayers();
		for(EntityPlayer player : players){
			TpmData data = UniEntity.get(player).getApp(TpmData.class);
			if(data != null) data.onInterval(lastinterval, passed);
		}
		lastinterval = Time.getDate();
	}

}
