package cyano.basemetals.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A pressure plate that only activates when a player steps on it
 */
public class BlockHumanDetector extends net.minecraft.block.BlockPressurePlate {

	public BlockHumanDetector() {
		super(Material.IRON, Sensitivity.MOBS);
	}

	@Override
	protected int computeRedstoneStrength(World w, BlockPos pos) {
		return (w.<Entity>getEntitiesWithinAABB(EntityPlayer.class, PRESSURE_AABB.offset(pos)).isEmpty()) ? 0 : 15;
	}

}