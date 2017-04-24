package com.mcmoddev.lib.item;

import com.mcmoddev.basemetals.init.Achievements;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Ingots
 *
 * @author DrCyano
 *
 */
public class ItemMMDIngot extends net.minecraft.item.Item implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material to make the ingot from
	 */
	public ItemMMDIngot(MMDMaterial material) {
		this.material = material;
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		crafter.addStat(Achievements.thisIsNew, 1);
	}

	@Override
	public MMDMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MMDMaterial getMetalMaterial() {
		return this.material;
	}
}
