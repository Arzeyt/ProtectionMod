package arzeyt.protectionMod;

import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.SidedProxy;

@NetworkMod(clientSideRequired=true,serverSideRequired=true //Whether client side and server side are needed
//clientPacketHandlerSpec = @SidedPacketHandler(channels = {"TutorialMod"}, packetHandler = ClientPacketHandler.class), //For clientside packet handling
//serverPacketHandlerSpec = @SidedPacketHandler(channels = {"TutorialMod"}, packetHandler = ServerPacketHandler.class) //For serverside packet handling
)

//MOD BASICS
@Mod(modid="ProtectionMod",name="ProtectionMod",version="Alpha")

public class ProtectionMod {

	@Instance("TutorialMod") //The instance, this is very important later on
	public static ProtectionMod instance = new ProtectionMod();
	
	@SidedProxy(clientSide = "arzeyt.protectionMod.ClientProxy", serverSide = "arzeyt.protectionMod.CommonProxy") //Tells Forge the location of your proxies
	public static CommonProxy proxy;
	
	public static Block protectionBlock;
	public static int protectionBlockId = 3000;
	public static Item protectionKey;
	public static String configDir;
	
	public static RegionHandler regionHandler;
	public static Logger myLogger;
	public static MessageSender messageSender;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent e){
	
		myLogger = FMLLog.getLogger();
		messageSender = new MessageSender();
		
		regionHandler = new RegionHandler(instance);
		
		configDir = e.getModConfigurationDirectory()+"ProtectionMod";
		
		//Blocks
		protectionBlock = new BlockProtectionBlock(protectionBlockId).setUnlocalizedName("ProtectionBlock"); //3000 is its ID. make configureable
	
		//Items
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event){
	
		//Blocks
		proxy.registerBlocks(); //Calls the registerBlocks method -- This wasn't here before, so don't skip over this!
		
		//Items
		
		//Events
		MinecraftForge.EVENT_BUS.register(new Listener());
		
		//MULTIPLAYER ABILITY
		NetworkRegistry.instance().registerGuiHandler(this, proxy); //Registers the class that deals with GUI data
	
	}
	
	
}
