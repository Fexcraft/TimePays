package net.fexcraft.mod.tpm.cap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public interface PlayerCapability {
	
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation("fsmm:player");
	
	public EntityPlayer getPlayer();
	
	public long getOnlineTime();
	
	public long getTotalOnlineTime();
	
	public Long getLastRewarded(String id);
	
	/*public Long getOnlineTimeForDimension(int dim);
	
	public Long getTotalOnlineTimeForDimension(int dim);*/

	public NBTTagCompound write();
	
	public void read(NBTTagCompound compound);

	public void copyFrom(PlayerCapability capability);
	
	public long getJoinTime();
	
	public long getLastLeftTime();

	public void onInterval(long interval, long passed);

}
