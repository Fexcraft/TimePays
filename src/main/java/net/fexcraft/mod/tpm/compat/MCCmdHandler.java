package net.fexcraft.mod.tpm.compat;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.tpm.Reward;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class MCCmdHandler implements RewardHandler {

	private static EntityPlayer PLAYER;
	private final ICommandSender sender = new ICommandSender() {
		@Override
		public String getName(){
			return "TPM:" + PLAYER.getName();
		}

		@Override
		public boolean canUseCommand(int permLevel, String commandName){
			return true;
		}

		@Override
		public World getEntityWorld(){
			return PLAYER.world;
		}

		@Override
		public MinecraftServer getServer(){
			return Static.getServer();
		}
	};

	@Override
	public void rewardPlayer(UniEntity player, Reward reward){
		JsonMap map = reward.reward;
		PLAYER = player.entity.local();
		String cmd = map.getString("command", "/give {PLAYER} minecraft:stone");
		cmd = cmd.replace("{PLAYER}", player.entity.getName());
		cmd = cmd.replace("{UUID}", player.entity.getUUID().toString());
		Static.getServer().commandManager.executeCommand(sender, cmd);
		if(map.has("message")){
			player.entity.send(map.get("message").string_value());
		}
	}

}
