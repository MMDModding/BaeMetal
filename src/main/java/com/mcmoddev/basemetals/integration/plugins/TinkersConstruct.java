package com.mcmoddev.basemetals.integration.plugins;

import java.util.Locale;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.data.TraitNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.TinkersConstructBase;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerTraitLocation;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkersMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.events.MaterialRegistrationEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.events.TinkersAlloyRecipeEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.events.TinkersExtraMeltingsEvent;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = TinkersConstruct.PLUGIN_MODID, versions = TinkersConstruct.PLUGIN_MODID
		+ "@[1.12.2-2.7.4.0,)")
public final class TinkersConstruct extends TinkersConstructBase implements IIntegration {

	public TinkersConstruct() {
		// do nothing
	}

	@Override
	public void init() {
		super.init();
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void materialRegistration(MaterialRegistrationEvent ev) {
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ADAMANTINE),
				MaterialNames.ADAMANTINE, true, false, ev, TraitNames.COLDBLOODED,
				TraitNames.INSATIABLE);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ANTIMONY), MaterialNames.ANTIMONY,
				true, false, ev);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.AQUARIUM), MaterialNames.AQUARIUM,
				true, false, ev, TraitNames.AQUADYNAMIC, TinkerTraitLocation.HEAD, TraitNames.JAGGED,
				TinkerTraitLocation.HEAD, TraitNames.AQUADYNAMIC);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.BISMUTH), MaterialNames.BISMUTH,
				true, false, ev);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.BRASS), MaterialNames.BRASS, true,
				false, ev, TraitNames.DENSE);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.COLDIRON), MaterialNames.COLDIRON,
				true, false, ev, TraitNames.FREEZING);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.CUPRONICKEL),
				MaterialNames.CUPRONICKEL, true, false, ev);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.INVAR), MaterialNames.INVAR, true,
				false, ev);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.MITHRIL), MaterialNames.MITHRIL,
				true, false, ev, TraitNames.HOLY);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.NICKEL), MaterialNames.NICKEL,
				true, false, ev);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.PEWTER), MaterialNames.PEWTER,
				true, false, ev, TraitNames.SOFT);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.PLATINUM), MaterialNames.PLATINUM,
				true, false, ev);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.STARSTEEL),
				MaterialNames.STARSTEEL, true, false, ev, TraitNames.ENDERFERENCE,
				TinkerTraitLocation.HEAD, TraitNames.SPARKLY);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.TIN), MaterialNames.TIN, true,
				false, ev);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ZINC), MaterialNames.ZINC, true,
				false, ev);
//		registerMyAlloys();
//		registerMeltings();
	}

	private void registerMaterial(final boolean active, final String name, final boolean castable, final boolean craftable,
			MaterialRegistrationEvent ev, final Object... traits) {
		if (active) {
			TinkersMaterial mat = new TinkersMaterial(Materials.getMaterialByName(name))
					.setCastable(castable).setCraftable(craftable).setToolForge(true);
			int i = 0;
			
			while(i < traits.length) {
				TinkerTraitLocation loc;
				String trait;
				Object item = traits[i];
				if(item instanceof TinkerTraitLocation) {
					loc = (TinkerTraitLocation)item;
					trait = (String)traits[++i];
					mat.addTrait(trait.toLowerCase(Locale.US), loc);
				} else {
					trait = (String)item;
					mat.addTrait(trait);
				}
				i++;
			}
			BaseMetals.logger.fatal("(A) Registering material %s (%s)", name, mat);
			ev.getRegistry().register(mat.create());
		}
	}

	private void registerMaterial(final boolean active, final String name, final boolean castable,
			final boolean craftable, MaterialRegistrationEvent ev) {
		if (active) {
			TinkersMaterial mat = new TinkersMaterial(Materials.getMaterialByName(name))
					.setCastable(castable).setCraftable(craftable).setToolForge(true).create();

			BaseMetals.logger.fatal("(B) Registering material %s (%s)", name, mat);
			ev.getRegistry().register(mat);
		}
	}

	@SubscribeEvent
	public void registerMeltings(TinkersExtraMeltingsEvent ev) {
		if (Materials.hasMaterial(MaterialNames.COAL)) {
			addExtraMelting(FluidRegistry.getFluidStack(MaterialNames.COAL, 144),
					new ItemStack(net.minecraft.init.Items.COAL));
		}

		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);
			addExtraMelting(FluidRegistry.getFluidStack(mercury.getName(), 144),
					mercury.getItemStack(Names.INGOT));
		}

		if (Materials.hasMaterial(MaterialNames.PRISMARINE)) {
			final MMDMaterial prismarine = Materials.getMaterialByName(MaterialNames.PRISMARINE);
			addExtraMelting(FluidRegistry.getFluidStack(prismarine.getName(), 144),
					prismarine.getItemStack(Names.INGOT));
		}
	}

	@SubscribeEvent
	public void registerMyAlloys(TinkersAlloyRecipeEvent ev) {
		if (Materials.hasMaterial(MaterialNames.AQUARIUM)
				&& Materials.hasMaterial(MaterialNames.COPPER)
				&& Materials.hasMaterial(MaterialNames.ZINC)) {
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 2);
			final FluidStack zinc = FluidRegistry.getFluidStack(MaterialNames.ZINC, 1);
			final FluidStack prismarine = FluidRegistry.getFluidStack(MaterialNames.PRISMARINE, 3);
			ev.addAlloyRecipe(MaterialNames.AQUARIUM, 3, copper, zinc, prismarine);
		}

		if (Materials.hasMaterial(MaterialNames.CUPRONICKEL)
				&& Materials.hasMaterial(MaterialNames.COPPER)
				&& Materials.hasMaterial(MaterialNames.NICKEL)) {
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 3);
			final FluidStack nickel = FluidRegistry.getFluidStack(MaterialNames.NICKEL, 1);
			ev.addAlloyRecipe(MaterialNames.CUPRONICKEL, 4, copper, nickel);
		}

		if (Materials.hasMaterial(MaterialNames.INVAR)
				&& Materials.hasMaterial(MaterialNames.NICKEL)) {
			final FluidStack iron = FluidRegistry.getFluidStack(MaterialNames.IRON, 2);
			final FluidStack nickel = FluidRegistry.getFluidStack(MaterialNames.NICKEL, 1);
			ev.addAlloyRecipe(MaterialNames.INVAR, 3, iron, nickel);
		}

		if (Materials.hasMaterial(MaterialNames.MITHRIL)
				&& Materials.hasMaterial(MaterialNames.COLDIRON)
				&& Materials.hasMaterial(MaterialNames.SILVER)
				&& Materials.hasMaterial(MaterialNames.MERCURY)) {
			final FluidStack coldiron = FluidRegistry.getFluidStack(MaterialNames.COLDIRON, 1);
			final FluidStack silver = FluidRegistry.getFluidStack(MaterialNames.SILVER, 2);
			final FluidStack mercury = FluidRegistry.getFluidStack(MaterialNames.MERCURY, 1);
			ev.addAlloyRecipe(MaterialNames.MITHRIL, 3, coldiron, silver, mercury);
		}

		if (Materials.hasMaterial(MaterialNames.PEWTER) && Materials.hasMaterial(MaterialNames.LEAD)
				&& Materials.hasMaterial(MaterialNames.COPPER)
				&& Materials.hasMaterial(MaterialNames.TIN)) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 2);
			final FluidStack tin = FluidRegistry.getFluidStack(MaterialNames.TIN, 137);
			final FluidStack lead = FluidRegistry.getFluidStack(MaterialNames.LEAD, 5);
			ev.addAlloyRecipe(MaterialNames.PEWTER, 144, copper, tin, lead);
		}

		if (Materials.hasMaterial(MaterialNames.STEEL)) {
			final FluidStack iron = FluidRegistry.getFluidStack(MaterialNames.IRON, 8);
			final FluidStack coal = FluidRegistry.getFluidStack(MaterialNames.COAL, 1);
			ev.addAlloyRecipe(MaterialNames.STEEL, 8, iron, coal);
		}
	}
}
