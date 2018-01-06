package com.mcmoddev.lib.item;

import com.mcmoddev.lib.block.BlockMMDAnvil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MMDContainerRepair extends ContainerRepair {
	private final BlockPos myPos;
	private final World myWorld;
	
	public MMDContainerRepair(InventoryPlayer playerInventory, World worldIn, EntityPlayer player) {
		super(playerInventory, worldIn, player);
		this.myPos = BlockPos.ORIGIN;
		this.myWorld = worldIn;
	}

	public MMDContainerRepair(InventoryPlayer playerInventory, World worldIn, BlockPos blockPosIn,
			EntityPlayer player) {
		super(playerInventory, worldIn, blockPosIn, player);
		this.myPos = blockPosIn;
		this.myWorld = worldIn;
	}

	@Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        if (!(this.myWorld.getBlockState(this.myPos).getBlock() instanceof BlockMMDAnvil) ) {
            return false;
        } else {
            return playerIn.getDistanceSq((double)this.myPos.getX() + 0.5D, (double)this.myPos.getY() + 0.5D, (double)this.myPos.getZ() + 0.5D) <= 64.0D;
        }
    }

}
