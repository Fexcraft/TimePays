package net.fexcraft.mod.tpm.compat;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fsmm.data.Manageable;
import net.fexcraft.mod.fsmm.data.PlayerAccData;
import net.fexcraft.mod.tpm.Reward;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.entity.player.EntityPlayer;

public class FSMMHandler {
	
	public static class Item implements RewardHandler {

		@Override
		public void rewardPlayer(EntityPlayer player, Reward reward){
			JsonMap map = reward.reward;
			PlayerAccData data = UniEntity.get(player).getApp(PlayerAccData.class);
			data.addMoneyToInventory(map.get("amount", 0));
			if(map.has("message")){
				Print.chat(player, map.get("message").string_value());
			}
		}
		
	}
	
	public static class Currency implements RewardHandler {

		@Override
		public void rewardPlayer(EntityPlayer player, Reward reward){
			JsonMap map = reward.reward;
			PlayerAccData data = UniEntity.get(player).getApp(PlayerAccData.class);
			data.getAccount().modifyBalance(Manageable.Action.ADD, map.getLong("amount", 0), UniEntity.getEntity(player));
			if(map.has("message")){
				Print.chat(player, map.get("message").string_value());
			}
		}
		
	}

}
