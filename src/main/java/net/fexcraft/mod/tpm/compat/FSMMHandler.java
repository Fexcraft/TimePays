package net.fexcraft.mod.tpm.compat;

import com.google.gson.JsonObject;

import net.fexcraft.lib.common.json.JsonUtil;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fsmm.api.FSMMCapabilities;
import net.fexcraft.mod.fsmm.api.Manageable.Action;
import net.fexcraft.mod.fsmm.api.PlayerCapability;
import net.fexcraft.mod.tpm.Reward;
import net.minecraft.entity.player.EntityPlayer;

public class FSMMHandler {
	
	public static class Item implements RewardHandler {

		@Override
		public void rewardPlayer(EntityPlayer player, Reward reward){
			JsonObject obj = reward.reward.getAsJsonObject();
			PlayerCapability cap = player.getCapability(FSMMCapabilities.PLAYER, null);
			cap.addMoneyToInventory(JsonUtil.getIfExists(obj, "amount", 0).longValue());
			if(obj.has("message")){
				Print.chat(player, obj.get("message").getAsString());
			}
		}
		
	}
	
	public static class Currency implements RewardHandler {

		@Override
		public void rewardPlayer(EntityPlayer player, Reward reward){
			JsonObject obj = reward.reward.getAsJsonObject();
			PlayerCapability cap = player.getCapability(FSMMCapabilities.PLAYER, null);
			cap.getAccount().modifyBalance(Action.ADD, JsonUtil.getIfExists(obj, "amount", 0).longValue(), player);
			if(obj.has("message")){
				Print.chat(player, obj.get("message").getAsString());
			}
		}
		
	}

}
