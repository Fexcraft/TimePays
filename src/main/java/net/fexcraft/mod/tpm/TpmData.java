package net.fexcraft.mod.tpm;

import net.fexcraft.lib.common.math.Time;
import net.fexcraft.mod.tpm.compat.RewardHandler;
import net.fexcraft.mod.uni.Appendable;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;

import java.util.TreeMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TpmData implements Appendable<UniEntity> {

	private UniEntity player;
	private long total = 0, session = 0, joined = 0, left = 0;
	private TreeMap<String, Long> received = new TreeMap<>();

	public TpmData(UniEntity unient){
		player = unient;
	}

	@Override
	public void save(UniEntity player, TagCW com){
		com.set("total", total);
		com.set("last", joined);
		for(String rew : received.keySet()){
			com.set("rew_" + rew, received.get(rew));
		}
		com.set("saved", Time.getDate());
	}

	@Override
	public void load(UniEntity player, TagCW com){
		total = com.getLong("total");
		left = com.getLong("last");
		joined = Time.getDate();
		for(String str : com.keys()){
			if(!str.startsWith("rew_")) continue;
			received.put(str.replace("rew_", ""), com.getLong(str));
		}
	}

	@Override
	public TpmData create(UniEntity unient){
		if(!unient.entity.isPlayer()) return null;
		return new TpmData(unient);
	}

	@Override
	public String id(){
		return "tpm";
	}

	public long getOnlineTime(){
		return session;
	}

	public long getTotalOnlineTime(){
		return total;
	}

	public Long getLastRewarded(String id){
		return received.get(id);
	}

	@Override
	public void copy(UniEntity old, Appendable<UniEntity> neo){
		if(!(neo instanceof TpmData)) return;
		TpmData data = (TpmData)neo;
		total = data.total;
		session = data.session;
		joined = data.joined;
		left = data.left;
		received = data.received;
	}

	public long getJoinTime(){
		return joined;
	}

	public long getLastLeftTime(){
		return left;
	}

	public void onInterval(long interval, long passed){
		total += passed;
		session += passed;
		for(Reward rew : RewardHandler.REWARDS.values()){
			try{
				if(rew.isApplicable(this, getLastRewarded(rew.id))){
					RewardHandler.HANDLERS.get(rew.handler).rewardPlayer(player, rew);
					received.put(rew.id, Time.getDate() - 1);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public UniEntity getPlayer(){
		return player;
	}

}
