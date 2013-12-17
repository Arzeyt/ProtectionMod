package arzeyt.protectionMod;


import java.util.Random;

import javax.jws.Oneway;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockProtectionBlock extends Block
{
        public BlockProtectionBlock(int par1)
        {
                super(par1, Material.rock); //You can set different materials, check them out!
                this.setCreativeTab(CreativeTabs.tabBlock);
        }
        
        @SideOnly(Side.CLIENT)
        public static Icon topIcon;
        @SideOnly(Side.CLIENT)
        public static Icon bottomIcon;
        @SideOnly(Side.CLIENT)
        public static Icon sideIconOff;
        @SideOnly(Side.CLIENT)
        public static Icon sideIconOn;

        @Override
        @SideOnly(Side.CLIENT)
        public void registerIcons(IconRegister icon) {
        topIcon = icon.registerIcon("protectionmod:ProtectionBlockTop");
        bottomIcon = icon.registerIcon("protectionmod:ProtectionBlockTop");
        sideIconOff = icon.registerIcon("protectionmod:ProtectionBlockSideOff");
        sideIconOn = icon.registerIcon("protectionmod:ProtectionBlockSideOn");
        }
       
        @Override
        @SideOnly(Side.CLIENT)
        public Icon getIcon(int side, int metadata) {
	        if(metadata==0){//block is off
	        	if(side == 0) {
	        		return bottomIcon;
		        } else if(side == 1) {
		        	return topIcon;
		        } else {
		        	return sideIconOff;
		        }
	        }else{//block is on. metadata==1
	        	if(side==0){
	        		return bottomIcon;
	        	}else if(side==1){
	        		return topIcon;
	        	}else{
	        		return sideIconOn;
	        	}
	        }
        }
        @SideOnly(Side.SERVER)
        @Override
        public boolean onBlockActivated(World world, int par2, int par3,
        	int par4, EntityPlayer player, int par6, float par7,
        	float par8, float par9) {
        	
        	ProtectionMod.regionHandler.handleRightClickProtectionBlock(world, player, par2, par3, par4);
        	
	        return super.onBlockActivated(world, par2, par3, par4, player,
	        		par6, par7, par8, par9);
        }
        
        @SideOnly(Side.SERVER)
        @Override
        public void onBlockPlacedBy(World world, int x, int y, int z,
        	EntityLivingBase entity, ItemStack par6ItemStack) {
        	
        	if(entity instanceof EntityPlayer){
        		EntityPlayer player = (EntityPlayer)entity;
        		ProtectionMod.messageSender.sendMessageToPlayer(player, "protection Block placed");
		        ProtectionMod.regionHandler.handleProtectionBlockPlace(player.getDisplayName(), world.provider.dimensionId, x, y, z);
        	}
        	
        	super.onBlockPlacedBy(world, x, y, z, entity,
	        		par6ItemStack);
        }
        
        @Override
        public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        	boolean powered = world.isBlockIndirectlyGettingPowered(x, y, z);
        	int metadata = world.getBlockMetadata(x, y, z);
        	
        	if(metadata==0 && powered == true){//block is being turned on
        		ProtectionMod.regionHandler.handleProtectionBlockActivate(world, x, y, z);
        	}else if(metadata==1 && powered == true){//block is being turned off
        		ProtectionMod.regionHandler.handleProtectionBlockDeactivate(world, x, y, z);
        	}

            
        super.onNeighborBlockChange(world, x, y, z, blockId);
        }
}