package net.fexcraft.mod.tpm.compat;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fsmm.data.Manageable;
import net.fexcraft.mod.fsmm.data.PlayerAccData;
import net.fexcraft.mod.tpm.Reward;
import net.fexcraft.mod.uni.UniEntity;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FSMMHandler {
	
	public static class Item implements RewardHandler {

		@Override
		public void rewardPlayer(UniEntity player, Reward reward){
			JsonMap map = reward.reward;
			PlayerAccData data = player.getApp(PlayerAccData.class);
			data.addMoneyToInventory(map.get("amount", 0));
			if(map.has("message")){
				player.entity.send(map.get("message").string_value());
			}
		}
		
	}
	
	public static class Currency implements RewardHandler {

		@Override
		public void rewardPlayer(UniEntity player, Reward reward){
			JsonMap map = reward.reward;
			PlayerAccData data = player.getApp(PlayerAccData.class);
			data.getAccount().modifyBalance(Manageable.Action.ADD, map.getLong("amount", 0), player.entity);
			if(map.has("message")){
				player.entity.send(map.get("message").string_value());
			}
		}
		
	}

}
