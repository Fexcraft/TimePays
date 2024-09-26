package net.fexcraft.mod.tpm.compat;

import com.google.gson.JsonObject;

import net.fexcraft.lib.common.json.JsonUtil;
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
			JsonObject obj = reward.reward.getAsJsonObject();
			PlayerAccData data = UniEntity.get(player).getApp(PlayerAccData.class);
			data.addMoneyToInventory(JsonUtil.getIfExists(obj, "amount", 0).longValue());
			if(obj.has("message")){
				Print.chat(player, obj.get("message").getAsString());
			}
		}
		
	}
	
	public static class Currency implements RewardHandler {

		@Override
		public void rewardPlayer(EntityPlayer player, Reward reward){
			JsonObject obj = reward.reward.getAsJsonObject();
			PlayerAccData data = UniEntity.get(player).getApp(PlayerAccData.class);
			data.getAccount().modifyBalance(Manageable.Action.ADD, JsonUtil.getIfExists(obj, "amount", 0).longValue(), UniEntity.getEntity(player));
			if(obj.has("message")){
				Print.chat(player, obj.get("message").getAsString());
			}
		}
		
	}

}
