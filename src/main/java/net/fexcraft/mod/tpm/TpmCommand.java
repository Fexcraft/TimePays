package net.fexcraft.mod.tpm;

import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.server.permission.PermissionAPI;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TpmCommand extends CommandBase {

	@Override
	public String getName(){
		return "tpm";
	}

	@Override
	public String getUsage(ICommandSender sender){
		return "/tpm <args>";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender){
		return true;
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args){
		boolean isp = sender instanceof EntityPlayer;
		if(args.length <= 0){
			Print.chat(sender, "&0[&bTPM&0]&a= = = = = = = = = = =");
			Print.chat(sender, "&bUser commands:");
			Print.chat(sender, "&7/tpm uuid");
			Print.chat(sender, "&7/tpm groups");
			Print.chat(sender, "&dAdmin commands:");
			Print.chat(sender, "&7/tpm add <player/uuid> <group>");
			Print.chat(sender, "&7/tpm rem <player/uuid> <group>");
			Print.chat(sender, "&7/tpm lookup <player/uuid>");
			Print.chat(sender, "&7/tpm clear <group>");
			Print.chat(sender, "&7/tpm reload (config)");
			return;
		}
		boolean op = isp ? server.isSinglePlayer() ? true : PermissionAPI.hasPermission((EntityPlayer)sender, TimePays.ADMIN_PERM) : true;
		switch(args[0]){
			case "uuid":{
				if(isp) Print.chat(sender, UniEntity.getEntity(sender).getId());
				return;
			}
			case "groups":{
				Print.chat(sender, "&0[&bTPM&0]&a Groups");
				for(Entry<String, ArrayList<UUID>> entry : TpmGroups.GROUPS.entrySet()){
					Print.chat(sender, entry.getKey() + " (" + entry.getValue().size() + " players)");
				}
				return;
			}
			case "add":{
				if(!op || args.length < 3) return;
				UUID uuid = getUUID(sender, server, args[1]);
				if(uuid == null) Print.chat(sender, "Player not found.");
				else{
					if(!TpmGroups.GROUPS.containsKey(args[2])){
						Print.chat(sender, "Created new group '" + args[2] + "'.");
						TpmGroups.GROUPS.put(args[2], new ArrayList<>());
					}
					if(TpmGroups.isInGroup(uuid, args[2])){
						Print.chat(sender, "Player is already in that group.");
					}
					else{
						TpmGroups.GROUPS.get(args[2]).add(uuid);
						Print.chat(sender, "Player added to group '" + args[2] + "'.");
						TpmGroups.save();
					}
				}
				return;
			}
			case "rem":{
				if(!op || args.length < 3) return;
				UUID uuid = getUUID(sender, server, args[1]);
				if(uuid == null) Print.chat(sender, "Player not found.");
				else{
					if(!TpmGroups.GROUPS.containsKey(args[2])){
						Print.chat(sender, "Group not found.");
					}
					if(!TpmGroups.isInGroup(uuid, args[2])){
						Print.chat(sender, "Player is not in that group.");
					}
					else{
						TpmGroups.GROUPS.get(args[2]).remove(uuid);
						Print.chat(sender, "Player removed from group '" + args[2] + "'.");
						TpmGroups.save();
					}
				}
				return;
			}
			case "lookup":{
				if(!op || args.length < 2) return;
				UUID uuid = getUUID(sender, server, args[1]);
				if(uuid == null) Print.chat(sender, "Player not found.");
				else{
					Print.chat(sender, "&0[&bTPM&0]&a " + args[1] + "'s groups:");
					boolean any = false;
					for(Entry<String, ArrayList<UUID>> entry : TpmGroups.GROUPS.entrySet()){
						if(entry.getValue().contains(uuid)){
							Print.chat(sender, entry.getKey());
							any = true;
						}
					}
					if(!any) Print.chat(sender, "Player is not in any reward groups.");
				}
				return;
			}
			case "clear":{
				if(!op || args.length < 2) return;
				if(!TpmGroups.GROUPS.containsKey(args[1])){
					Print.chat(sender, "Group not found.");
				}
				else{
					TpmGroups.GROUPS.remove(args[1]);
					Print.chat(sender, "Group '" + args[1] + "' removed.");
					TpmGroups.save();
				}
				return;
			}
			case "reload":{
				if(!op) return;
				TimePays.CONFIG.reload();
				Print.chat(sender, "&0[&bTPM&0]&a Config reloaded.");
				Print.chat(sender, "&bINFO: This does NOT restart the interval timer.");
				return;
			}
			default:
				break;
		}
		Print.chat(sender, "&cInvalid Argument.");
	}

	private UUID getUUID(ICommandSender sender, MinecraftServer server, String arg){
		if(arg.equals("@p")){
			EntityPlayer player = null;
			double closest = Integer.MAX_VALUE;
			double dis;
			Vec3d vec = sender.getPositionVector();
			for(EntityPlayerMP pl : server.getPlayerList().getPlayers()){
				if(pl.world != sender.getEntityWorld()) continue;
				dis = pl.getDistance(vec.x, vec.y, vec.z);
				if(dis < closest){
					player = pl;
					closest = dis;
				}
			}
			if(player != null) return player.getGameProfile().getId();
		}
		try{
			return UUID.fromString(arg);
		}
		catch(Exception e0){
			try{
				return server.getPlayerProfileCache().getGameProfileForUsername(arg).getId();
			}
			catch(Exception e1){
				e1.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public boolean isUsernameIndex(String[] args, int idx){
		return args.length > 0 && (args[0].equals("add") || args[0].equals("rem"));
	}

}

