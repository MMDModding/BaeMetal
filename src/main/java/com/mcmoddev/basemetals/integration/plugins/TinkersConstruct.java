package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.data.TraitNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import static com.mcmoddev.lib.integration.plugins.tinkers.TinkerMaterial.TinkersTraitLocation;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = TinkersConstruct.PLUGIN_MODID,
           versions=TinkersConstruct.PLUGIN_MODID+"@[2.7.4,)")
public final class TinkersConstruct
		extends com.mcmoddev.lib.integration.plugins.TinkersConstructBase implements IIntegration {

	public TinkersConstruct() {
		super();
	}
	
	@Override
	public void init() {
		super.init();
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		MinecraftForge.EVENT_BUS.register(this);
		
		this.registerMaterial(MaterialNames.ADAMANTINE, true, false, TraitNames.COLDBLOODED,
				TraitNames.INSATIABLE);
		registerMaterial(MaterialNames.ANTIMONY, true, false);
		this.registerMaterial(MaterialNames.AQUARIUM, true, false, TraitNames.AQUADYNAMIC,
				TinkersTraitLocation.HEAD, TraitNames.JAGGED, TinkersTraitLocation.HEAD,
				TraitNames.AQUADYNAMIC);
		registerMaterial(MaterialNames.BISMUTH, true, false);
		this.registerMaterial(MaterialNames.BRASS, true, false, TraitNames.DENSE);
		this.registerMaterial(MaterialNames.COLDIRON, true, false, TraitNames.FREEZING);
		registerMaterial(MaterialNames.CUPRONICKEL, true, false);
		registerMaterial(MaterialNames.INVAR, true, false);
		this.registerMaterial(MaterialNames.MITHRIL, true, false, TraitNames.HOLY);
		registerMaterial(MaterialNames.NICKEL, true, false);
		this.registerMaterial(MaterialNames.PEWTER, true, false, TraitNames.SOFT);
		registerMaterial(MaterialNames.PLATINUM, true, false);
		this.registerMaterial(MaterialNames.STARSTEEL, true, false, TraitNames.ENDERFERENCE,
				TinkersTraitLocation.HEAD, TraitNames.SPARKLY);
		registerMaterial(MaterialNames.TIN, true, false);
		registerMaterial(MaterialNames.ZINC, true, false);
		registerAlloys();
		registerMeltings();
	}

	private void registerMeltings() {
		if (Materials.hasMaterial(MaterialNames.COAL)) {
			registerMelting(new ItemStack(net.minecraft.init.Items.COAL),
					FluidRegistry.getFluidStack(MaterialNames.COAL, 144));
		}

		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);
			registerMelting(mercury.getItemStack(Names.INGOT),
					FluidRegistry.getFluidStack(mercury.getName(), 144));
		}

		if (Materials.hasMaterial(MaterialNames.PRISMARINE)) {
			final MMDMaterial prismarine = Materials.getMaterialByName(MaterialNames.PRISMARINE);
			registerMelting(new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD),
					FluidRegistry.getFluidStack(prismarine.getName(), 666));
		}
	}
	
	private void registerAlloys() {
		if (Materials.hasMaterial(MaterialNames.AQUARIUM)
				&& Materials.hasMaterial(MaterialNames.COPPER)
				&& Materials.hasMaterial(MaterialNames.ZINC)) {
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.AQUARIUM, 3);
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 2);
			final FluidStack zinc = FluidRegistry.getFluidStack(MaterialNames.ZINC, 1);
			final FluidStack prismarine = FluidRegistry.getFluidStack(MaterialNames.PRISMARINE, 3);
			registerAlloy(MaterialNames.AQUARIUM, output, copper, zinc, prismarine);
		}

		if (Materials.hasMaterial(MaterialNames.CUPRONICKEL)
				&& Materials.hasMaterial(MaterialNames.COPPER)
				&& Materials.hasMaterial(MaterialNames.NICKEL)) {
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.CUPRONICKEL, 4);
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 3);
			final FluidStack nickel = FluidRegistry.getFluidStack(MaterialNames.NICKEL, 1);
			registerAlloy(MaterialNames.CUPRONICKEL, output, copper, nickel);
		}

		if (Materials.hasMaterial(MaterialNames.INVAR)
				&& Materials.hasMaterial(MaterialNames.NICKEL)) {
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.INVAR, 3);
			final FluidStack iron = FluidRegistry.getFluidStack(MaterialNames.IRON, 2);
			final FluidStack nickel = FluidRegistry.getFluidStack(MaterialNames.NICKEL, 1);
			registerAlloy(MaterialNames.INVAR, output, iron, nickel);
		}

		if (Materials.hasMaterial(MaterialNames.MITHRIL)
				&& Materials.hasMaterial(MaterialNames.COLDIRON)
				&& Materials.hasMaterial(MaterialNames.SILVER)
				&& Materials.hasMaterial(MaterialNames.MERCURY)) {
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.MITHRIL, 3);
			final FluidStack coldiron = FluidRegistry.getFluidStack(MaterialNames.COLDIRON, 1);
			final FluidStack silver = FluidRegistry.getFluidStack(MaterialNames.SILVER, 2);
			final FluidStack mercury = FluidRegistry.getFluidStack(MaterialNames.MERCURY, 1);
			registerAlloy(MaterialNames.MITHRIL, output, coldiron, silver, mercury);
		}

		if (Materials.hasMaterial(MaterialNames.PEWTER) && Materials.hasMaterial(MaterialNames.LEAD)
				&& Materials.hasMaterial(MaterialNames.COPPER)
				&& Materials.hasMaterial(MaterialNames.TIN)) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.PEWTER, 144);
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 2);
			final FluidStack tin = FluidRegistry.getFluidStack(MaterialNames.TIN, 137);
			final FluidStack lead = FluidRegistry.getFluidStack(MaterialNames.LEAD, 5);
			registerAlloy(MaterialNames.PEWTER, output, copper, tin, lead);
		}

		if (Materials.hasMaterial(MaterialNames.STEEL)) {
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.STEEL, 8);
			final FluidStack iron = FluidRegistry.getFluidStack(MaterialNames.IRON, 8);
			final FluidStack coal = FluidRegistry.getFluidStack(MaterialNames.COAL, 1);
			registerAlloy(MaterialNames.STEEL, output, iron, coal);
		}
	}	
}
