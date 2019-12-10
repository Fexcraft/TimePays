package net.fexcraft.mod.tpm.compat;

import java.util.TreeMap;

import net.fexcraft.mod.tpm.Reward;
import net.minecraft.entity.player.EntityPlayer;

public interface RewardHandler {
	
	public static final TreeMap<String, RewardHandler> HANDLERS = new TreeMap<>();
	public static final TreeMap<String, Reward> REWARDS = new TreeMap<>();
	
	public void rewardPlayer(EntityPlayer player, Reward reward);

}
