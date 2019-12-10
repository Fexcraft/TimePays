package net.fexcraft.mod.tpm.cap;

import java.util.TreeMap;

import net.fexcraft.lib.common.math.Time;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityContainer implements ICapabilitySerializable<NBTBase>{
	
	@CapabilityInject(PlayerCapability.class)
	private PlayerCapability instance;

	@CapabilityInject(PlayerCapability.class)
	public static final Capability<PlayerCapability> PLAYER = null;
	
	public CapabilityContainer(EntityPlayer player){
		instance = ((Implementation)PLAYER.getDefaultInstance()).setEntityPlayer(player);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		return capability == PLAYER;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		return capability == PLAYER ? PLAYER.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT(){
		return PLAYER.getStorage().writeNBT(PLAYER, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt){
		PLAYER.getStorage().readNBT(PLAYER, instance, null, nbt);
	}
	
	public static class Storage implements IStorage<PlayerCapability> {

		@Override
		public NBTBase writeNBT(Capability<PlayerCapability> capability, PlayerCapability instance, EnumFacing side){
			return instance.write();
		}

		@Override
		public void readNBT(Capability<PlayerCapability> capability, PlayerCapability instance, EnumFacing side, NBTBase nbt){
			//instance.read((NBTTagCompound)nbt);
		}
		
	}
	
	public static class Callable implements java.util.concurrent.Callable<PlayerCapability>{

		@Override
		public PlayerCapability call() throws Exception {
			return new Implementation();
		}
		
	}
	
	public static class Implementation implements PlayerCapability {
		
		@SuppressWarnings("unused")
		private EntityPlayer player;
		private long total = 0, session = 0, joined = 0, last = 0;
		private TreeMap<Integer, Long> dimensions = new TreeMap<>();
		private TreeMap<Integer, Long> dim_totals = new TreeMap<>();
		private TreeMap<String, Long> received = new TreeMap<>();

		public PlayerCapability setEntityPlayer(EntityPlayer player){
			this.player = player; return this;
		}

		@Override
		public long getOnlineTime(){
			return session;
		}

		@Override
		public long getTotalOnlineTime(){
			return total;
		}

		@Override
		public Long getLastRewarded(String id){
			return received.get(id);
		}

		@Override
		public Long getOnlineTimeForDimension(int dim){
			return dimensions.get(dim);
		}

		@Override
		public Long getTotalOnlineTimeForDimension(int dim){
			return dim_totals.get(dim);
		}

		@Override
		public NBTTagCompound write(){
			NBTTagCompound compound = new NBTTagCompound();
			compound.setLong("total", total);
			compound.setLong("last", joined);
			for(int dim : dim_totals.keySet()){
				compound.setLong("dim_" + dim, dim_totals.get(dim));
			}
			for(String rew : received.keySet()){
				compound.setLong("rew_" + rew, received.get(rew));
			}
			compound.setLong("saved", Time.getDate());
			return compound;
		}

		@Override
		public void read(NBTTagCompound compound){
			if(compound == null) return;
			total = compound.getLong("total");
			last = compound.getLong("last");
			joined = Time.getDate();
			for(String str : compound.getKeySet()){
				if(str.startsWith("dim_")){
					int dim = Integer.parseInt(str.replace("dim_", ""));
					dimensions.put(dim, 0l); dim_totals.put(dim, compound.getLong(str));
				}
				else if(str.startsWith("rew_")){
					received.put(str.replace("rew_", ""), compound.getLong(str));
				}
				else continue;
			}
		}

		@Override
		public void copyFrom(PlayerCapability capraw){
			Implementation capability = (Implementation)capraw;
			total = capability.total; session = capability.session;
			joined = capability.joined; last = capability.last;
			dimensions = capability.dimensions;
			dim_totals = capability.dim_totals;
			received = capability.received;
		}

		@Override
		public long getJoinTime(){
			return joined;
		}

		@Override
		public long getLastLeftTime(){
			return last;
		}

		@Override
		public void onInterval(){
			//TODO
		}
		
	}

}
