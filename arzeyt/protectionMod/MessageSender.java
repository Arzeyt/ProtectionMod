package arzeyt.protectionMod;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;

public class MessageSender {

	public void sendMessageToPlayer(EntityPlayer player, String msg){
		player.sendChatToPlayer(new ChatMessageComponent().createFromText(msg));
	}
	
	/**
	 * probably only works on servers...
	 * @param playerName
	 * @param msg
	 */
	public void sendMessageToPlayer(String playerName, String msg){
		ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for(EntityPlayer player : players){
			if(player.getDisplayName().equals(playerName)){
				sendMessageToPlayer(player, msg);
			}
		}
	}
}
