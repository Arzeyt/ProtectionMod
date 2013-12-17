package arzeyt.protectionMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class Listener {

	@SideOnly(Side.SERVER)
	@ForgeSubscribe
	public void onBlockBreak(BreakEvent event){
		ProtectionMod.messageSender.sendMessageToPlayer(event.getPlayer(), "break event passed");
		
		int x = event.x;
		int y = event.y;
		int z = event.z;
		
		try{
			ProtectionRegion region = ProtectionMod.regionHandler.getRegionForCoords(event.world, event.x, event.y, event.z);
			if(region.active == true && region.isUser(event.getPlayer().getDisplayName())==false){
				event.setCanceled(true);
			}
		}catch(Exception e){
			
		}
	}
}
