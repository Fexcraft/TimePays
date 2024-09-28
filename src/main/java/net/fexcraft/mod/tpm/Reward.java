package net.fexcraft.mod.tpm;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.Time;

import java.util.ArrayList;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Reward {
	
	public final String id;
	public final String handler;
	public final ArrayList<String> groups;
	public final boolean session;
	public final boolean onetime;
	public final long interval;
	public final JsonMap reward;
	
	public Reward(String rid, JsonMap map){
		id = rid;
		handler = map.getString("type", "none");
		session = map.getBoolean("session", !map.getBoolean("total", false));
		interval = map.getLong("interval", 10) * Time.MIN_MS;
		groups = map.has("groups") ? map.getArray("groups").toStringList() : null;
		reward = map.getMap("reward");
		onetime = map.getBoolean("one-time", false);
	}

	public boolean isApplicable(TpmData data, Long last_reward){
		if(onetime && last_reward != null) return false;
		else if(last_reward == null) last_reward = data.getJoinTime();
		if(groups != null && !TpmGroups.isInAnyGroup(data.getPlayer().entity.getUUID(), groups)) return false;
		if(!session){//total
			if(onetime && data.getTotalOnlineTime() >= interval) return true;
			if(!onetime && Time.getDate() - last_reward >= interval) return true;
		}
		else{
			if(onetime && data.getOnlineTime() >= interval) return true;
			if(!onetime && Time.getDate() - last_reward >= interval) return true;
		}
		return false;
	}

}
