package arzeyt.protectionMod;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;

/**
 * 
 * @info Singleton, static
 */

public class RegionHandler {

	ProtectionMod instance;
	MessageSender messageSender;
	
	ArrayList<ProtectionRegion> regions = new ArrayList<ProtectionRegion>();
	HashMap<Integer, ArrayList<ProtectionRegion>> dimRegions = new HashMap<Integer, ArrayList<ProtectionRegion>>();
	public RegionHandler(ProtectionMod instance){
		this.instance = instance;
		this.messageSender = instance.messageSender;
	}
	
	/**
	 * Adds region to appropriate maps. Be sure to check for collision.
	 * @param region
	 */
	public void addRegion(ProtectionRegion region){
		
			regions.add(region);
			dimRegions.put(region.dimension, regions);
	}

	/**
	 * 
	 * @param region -a fresh region object to test for collision with other regions prior to adding to
	 * the game in further processes.
	 * @return true if region can be created.
	 */
	private boolean canCreate(ProtectionRegion region) {
		if(regionsCollidesWithRegion(region)){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * Adds the player to the region's user list.
	 * @param world
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 */
	
	public void handleRightClickProtectionBlock(World world, EntityPlayer player, int x, int y, int z) {
		
		boolean regionExists = false;
		ProtectionRegion region = null;
		
		if(world.isRemote==false){
			region = getRegionForCoords(world, x, y, z);
			if(region.users.contains(player.getDisplayName())==false){
				region.addUser(player.getDisplayName());
				player.sendChatToPlayer(new ChatMessageComponent().createFromText("Added to user list"));
			}
		}
	}
	
	public void handleProtectionBlockActivate(World world, int x, int y, int z){
		if(world.isRemote==false){
			ProtectionRegion region = getRegionForCoords(world, x, y, z);
			
			if(region!=null){
				world.setBlockMetadataWithNotify(x, y, z, 1, 2);//2 means the client should be notified
				region.active = true;
			}
			//handle region not existing.
		}
	}
	
	public void handleProtectionBlockDeactivate(World world, int x, int y, int z){
		if(world.isRemote==false){
			ProtectionRegion region = getRegionForCoords(world, x, y, z);
			
			if(region!=null){
				world.setBlockMetadataWithNotify(x, y, z, 0, 2);
				region.active=false;
			}
		}
	}

	/**
	 * @param ownerName
	 * @param dimension block's dimension
	 * @param x block's x position
	 * @param y block's y position
	 * @param z block's z position
	 * @return true if block place was successful, false if block place was unsuccessful
	 */
	public boolean handleProtectionBlockPlace(String ownerName, int dimension, int x, int y, int z){
		int radius = 10;
		ProtectionRegion region = new ProtectionRegion(ownerName, dimension, x, y, z, radius, false);
		ProtectionMod.messageSender.sendMessageToPlayer(ownerName, "Handling protection block place for: "
				+ "new Region: "+x+" "+y+" "+z);
	
		if(canCreate(region)==false){
			messageSender.sendMessageToPlayer(ownerName, "can't create region");
			return false;
		}else{
			messageSender.sendMessageToPlayer(ownerName, "created region");
			addRegion(region);
			return true;
		}
	}
	
	/**
	 * 
	 * @param dim
	 * @param x
	 * @param y
	 * @return region that this location collides with. null if no collision.
	 */
	public ProtectionRegion pointCollidesWithRegion(int dim, int x, int y){
		for(ProtectionRegion region: dimRegions.get(dim)){
			if(region.posX-region.radius<=x && x<=region.posX+region.radius &&
					region.posZ-region.radius<=y && y<=region.posZ+region.radius){
				return region;
			}
		}
		return null;
	}
	
	/**
	 * true if region param collides with any region
	 * @param region
	 * @return
	 */
	public boolean regionsCollidesWithRegion(ProtectionRegion region){
		boolean xCheck = true;
		boolean yCheck = true;
		
		try{
			for(ProtectionRegion regionE : dimRegions.get(region.dimension)){
				 if(regionE.posX < region.posX){
					 if ((regionE.posX+regionE.radius) < (region.posX-region.radius)){
						 xCheck=false;
					 }
				 }else if(regionE.posX > region.posX){
					 if ((regionE.posX-regionE.radius) > (region.posX + region.radius)){
						 xCheck=false;
					 }
				 }
				 if(regionE.posZ < region.posZ){
					 if((regionE.posZ+regionE.radius) < (region.posZ-region.radius)){
						 yCheck=false;
					 }
				 }else if(regionE.posZ > region.posY){
					 if((regionE.posZ-regionE.radius) > (region.posZ+region.radius)){
						 yCheck = false;
					 }
				 }
			 }
			
			 messageSender.sendMessageToPlayer(region.ownerName, "xCheck is "+xCheck+": zCheck is"+yCheck);
			 if(xCheck==false && yCheck==false){//if both x and y do no collide
				 return false;
			 }else{
				 return true;
			 }
		}catch(Exception e){//no regions existed
			return false;
		}
	}
	
	public ProtectionRegion getRegionForCoords(World world, int x, int y, int z){
		for(ProtectionRegion region : dimRegions.get(world.provider.dimensionId)){
			if(region.posX==x && region.posY==y && region.posZ==z){
				return region;
			}
		}
		return null;
	}
	
}
