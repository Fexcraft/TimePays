package net.fexcraft.mod.tpm.compat;

import com.google.gson.JsonObject;

import net.fexcraft.lib.common.json.JsonUtil;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.tpm.Reward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

public class MCItemHandler implements RewardHandler {

	@Override
	public void rewardPlayer(EntityPlayer player, Reward reward){
		JsonObject obj = reward.reward.getAsJsonObject();
		Item item = Item.getByNameOrId(JsonUtil.getIfExists(obj, "item", "minecraft:stone"));
		ItemStack stack = new ItemStack(item, JsonUtil.getIfExists(obj, "count", 1).intValue(), JsonUtil.getIfExists(obj, "meta", 0).intValue());
		if(obj.has("nbt")){
			try {
				stack.setTagCompound(JsonToNBT.getTagFromJson(obj.get("nbt").toString()));
			}
			catch(NBTException e){
				e.printStackTrace();
			}
		}
		player.addItemStackToInventory(stack);
		if(obj.has("message")){
			Print.chat(player, obj.get("message").getAsString());
		}
	}

}
