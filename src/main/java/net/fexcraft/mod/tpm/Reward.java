package net.fexcraft.mod.tpm;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.json.JsonUtil;
import net.fexcraft.lib.common.math.Time;
import net.fexcraft.mod.tpm.cap.PlayerCapability;

public class Reward {
	
	public final String id, handler;
	public final boolean session, onetime;
	public final long interval;
	public final JsonMap reward;
	
	public Reward(String rid, JsonMap map){
		id = rid;
		handler = map.getString("type", "none");
		session = !map.getBoolean("total", false);
		String type = map.getString("interval-type", "m");
		long multiplier = 1;
		switch(type){
			case "ms": multiplier = 1;
			case "m": multiplier = Time.MIN_MS; break;
			case "s": multiplier = Time.SEC_MS; break;
			case "h": multiplier = Time.HOUR_MS; break;
			case "d": multiplier = Time.DAY_MS; break;
		}
		interval = map.getLong("interval-time", 10) * multiplier;
		reward = map.getMap("reward");
		onetime = map.getBoolean("one-time", false);
	}

	public boolean isApplicable(PlayerCapability player, Long last_reward){
		if(onetime && last_reward != null) return false;
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
