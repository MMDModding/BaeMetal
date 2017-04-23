package com.mcmoddev.lib.item;

import java.util.List;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Axes
 * 
 * @author DrCyano
 *
 */
public class ItemMMDAxe extends ItemAxe implements IMMDObject {

	protected final MMDMaterial material;
	protected final String repairOreDictName;
	protected final boolean regenerates;
	protected static final long REGEN_INTERVAL = 200;

	/**
	 *
	 * @param material
	 *            The material to make the axe from
	 */
	public ItemMMDAxe(MMDMaterial material) {
		super(Materials.getToolMaterialFor(material));
		this.material = material;
		this.setMaxDamage(this.material.getToolDurability());
		this.damageVsEntity = 4F + (2F * this.material.getBaseAttackDamage());
		this.attackSpeed = -3.5F + Math.min(0.5F, 0.05F * this.material.getStat(MaterialStats.STRENGTH));
		this.efficiencyOnProperMaterial = this.material.getToolEfficiency();
		this.repairOreDictName = "ingot" + this.material.getCapitalizedName();
		this.regenerates = this.material.regenerates();
	}

	@Override
	public ToolMaterial getToolMaterial() {
		return this.toolMaterial;
	}

	@Override
	public int getItemEnchantability() {
		return this.toolMaterial.getEnchantability();
	}

	@Override
	public String getToolMaterialName() {
		return this.toolMaterial.toString();
	}

	@Override
	public boolean getIsRepairable(final ItemStack intputItem, final ItemStack repairMaterial) {
		final List<ItemStack> acceptableItems = OreDictionary.getOres(this.repairOreDictName);
		for (final ItemStack i : acceptableItems)
			if (ItemStack.areItemsEqual(i, repairMaterial))
				return true;
		return false;
	}

	@Override
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		super.hitEntity(item, target, attacker);
		MMDToolEffects.extraEffectsOnAttack(this.material, item, target, attacker);
		return true;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MMDToolEffects.extraEffectsOnCrafting(this.material, item, world, crafter);
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if (this.regenerates && !world.isRemote && isHeld && (item.getItemDamage() > 0) && ((world.getTotalWorldTime() % REGEN_INTERVAL) == 0))
			item.setItemDamage(item.getItemDamage() - 1);
	}
/*
	public String getMaterialName() {
		return this.material.getName();
	}
*/
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

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean b) {
		super.addInformation(stack, player, list, b);
		MMDToolEffects.addToolSpecialPropertiesToolTip(this.material, list);
	}
}
