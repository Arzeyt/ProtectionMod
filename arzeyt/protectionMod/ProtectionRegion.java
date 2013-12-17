package arzeyt.protectionMod;

import java.io.Serializable;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

/**
 * 
 * @author Default
 * @info Region object. Represents a single region, and can be serialized. 
 * Make sure to keep an updated user list
 */
public class ProtectionRegion implements Serializable{

	private static final long serialVersionUID = -6472581807338270378L;

	public String ownerName;
	public Integer dimension, posX, posY, posZ, id, radius;
	public boolean active;
	
	public ArrayList<String> users = new ArrayList<String>();

	public ProtectionRegion(String playerName, int dimension, int x, int y, int z, int radius, boolean active) {

		this.ownerName = playerName;

		this.dimension = dimension;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.radius = radius;
		
		this.active=active;
		
	}

	/**
	 * 
	 * @return EntityPlayer if player is online and null if he isn't.
	 * 
	 */
	public EntityPlayer getOwner(){
		ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for(EntityPlayer player : players){
			if(player.getDisplayName().equals(ownerName)){
				return player;
			}
		}
		return null;

	}
	
	public void addUser(String playerName){
		this.users.add(playerName);
	}
	
	public void removeUser(String playerName){
		ArrayList<String> temp = new ArrayList<String>();
		temp = users;
		temp.remove(playerName);
		users = temp;
	}
	
	public boolean isUser(String playername){
		for(String name : users){
			if(playername.equals(name)){
				return true;
			}
		}
		return false;
	}

}
