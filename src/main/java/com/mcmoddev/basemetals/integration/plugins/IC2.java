package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mcmoddev.lib.integration.plugins.IC2Base;
import com.mcmoddev.lib.material.MMDMaterial;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = IC2.PLUGIN_MODID)
public class IC2 extends IC2Base implements IIntegration {

	private static boolean initDone = false;
	
	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(IC2.PLUGIN_MODID)) {
			return;
		}
		
		MinecraftForge.EVENT_BUS.register(this);
		
		initDone = true;
	}
	
    @SubscribeEvent
    public void regCallback(RegistryEvent.Register<IRecipe> ev) {
		final String[] baseNames = new String[] {
				MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH,
				MaterialNames.COLDIRON,
				MaterialNames.PLATINUM,
				MaterialNames.NICKEL,
				MaterialNames.STARSTEEL,
				MaterialNames.ZINC
		};

		for (int i = 0; i < baseNames.length; i++) {
			final String materialName = baseNames[i];
			if (Options.isMaterialEnabled(materialName)) {
				registerVanillaRecipes(materialName);
				addMaceratorRecipes(materialName);
				addOreWashingPlantRecipes(materialName);
				addThermalCentrifugeRecipes(materialName);
				addMetalFormerRecipes(materialName);
				addCompressorRecipes(materialName);
			}
		}
		
		if( Options.isMaterialEnabled(MaterialNames.DIAMOND) ) {
			MMDMaterial diamond = Materials.getMaterialByName(MaterialNames.DIAMOND);
			String oreDictName = diamond.getCapitalizedName();
			addMaceratorRecipe( Oredicts.ORE + oreDictName, new ItemStack(diamond.getItem(Names.POWDER), 2) );
		}
		
		if( Options.isMaterialEnabled(MaterialNames.EMERALD) ) {
			MMDMaterial emerald = Materials.getMaterialByName(MaterialNames.EMERALD);
			String oreDictName = emerald.getCapitalizedName();
			addMaceratorRecipe( Oredicts.ORE + oreDictName, new ItemStack(emerald.getItem(Names.POWDER), 2) );
		}

    }

}
