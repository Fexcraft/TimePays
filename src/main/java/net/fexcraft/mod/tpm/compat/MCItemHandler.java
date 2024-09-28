package net.fexcraft.mod.tpm.compat;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.tpm.Reward;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

public class MCItemHandler implements RewardHandler {

	@Override
	public void rewardPlayer(UniEntity player, Reward reward){
		JsonMap map = reward.reward;
		Item item = Item.getByNameOrId(map.getString("item", "minecraft:stone"));
		ItemStack stack = new ItemStack(item, map.getInteger("count", 1), map.getInteger("meta", 0));
		if(map.has("nbt")){
			try {
				stack.setTagCompound(JsonToNBT.getTagFromJson(map.get("nbt").toString()));
			}
			catch(NBTException e){
				e.printStackTrace();
			}
		}
		EntityPlayer ep = player.entity.local();
		ep.addItemStackToInventory(stack);
		if(map.has("message")){
			player.entity.send(map.get("message").string_value());
		}
	}

}
