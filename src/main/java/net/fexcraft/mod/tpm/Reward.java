package net.fexcraft.mod.tpm;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.json.JsonUtil;
import net.fexcraft.lib.common.math.Time;
import net.fexcraft.mod.tpm.cap.PlayerCapability;

public class Reward {
	
	public final String id, handler;
	public final boolean session, onetime;
	public final long interval;
	public final JsonElement reward;
	
	public Reward(JsonObject obj){
		id = JsonUtil.getIfExists(obj, "id", "noid_" + Static.random.nextInt(9999));
		handler = JsonUtil.getIfExists(obj, "handler", "none");
		session = !JsonUtil.getIfExists(obj, "total", false);
		String type = JsonUtil.getIfExists(obj, "interval_type", "m");
		long multiplier = 1;
		switch(type){
			case "ms": multiplier = 1;
			case "m": multiplier = Time.MIN_MS; break;
			case "s": multiplier = Time.SEC_MS; break;
			case "h": multiplier = Time.HOUR_MS; break;
			case "d": multiplier = Time.DAY_MS; break;
		}
		interval = JsonUtil.getIfExists(obj, "interval_time", 10).longValue() * multiplier;
		reward = obj.get("reward"); onetime = JsonUtil.getIfExists(obj, "one_time", false);
	}

	public boolean isApplicable(PlayerCapability player, Long last_reward){
		if(onetime && !session && last_reward != null) return false;
		else if(last_reward == null) last_reward = player.getJoinTime();
		if(!session){//total
			if(onetime && player.getTotalOnlineTime() >= interval) return true;
			if(!onetime && Time.getDate() - last_reward >= interval) return true;
		}
		else{
			if(onetime && player.getOnlineTime() >= interval) return true;
			if(!onetime && Time.getDate() - last_reward >= interval) return true;
		}
		return false;
	}

}
