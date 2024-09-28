package net.fexcraft.mod.tpm.compat;

import java.util.TreeMap;

import net.fexcraft.mod.tpm.Reward;
import net.fexcraft.mod.uni.UniEntity;

public interface RewardHandler {
	
	public static final TreeMap<String, RewardHandler> HANDLERS = new TreeMap<>();
	public static final TreeMap<String, Reward> REWARDS = new TreeMap<>();
	
	public void rewardPlayer(UniEntity player, Reward reward);

}
