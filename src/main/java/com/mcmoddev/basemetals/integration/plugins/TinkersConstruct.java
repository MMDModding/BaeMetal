package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.data.TraitNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.IntegrationInitEvent;
import com.mcmoddev.lib.integration.IntegrationPostInitEvent;
import com.mcmoddev.lib.integration.IntegrationPreInitEvent;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerMaterial;
import static com.mcmoddev.lib.integration.plugins.tinkers.TinkerMaterial.TinkersTraitLocation;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = TinkersConstruct.PLUGIN_MODID, versions="tconstruct@[2.7.4.0,)")
public final class TinkersConstruct
		extends com.mcmoddev.lib.integration.plugins.TinkersConstructBase implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

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

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void preInit(IntegrationPreInitEvent ev) {
		
	}
	
	@SubscribeEvent
	public void init(IntegrationInitEvent ev) {
		
	}

	@SubscribeEvent
	public void postInit(IntegrationPostInitEvent ev) {
		
	}

	/**
	 *
	 *
	public void preInit() {
		if (this.preInit) {
			return;
		}
		this.preInit = true;
		this.preInitSetup();
		this.registerMelting();
		this.setMaterialsVisible(BaseMetals.MODID);
	}

	/**
	 *
	 *
	public void initCallback() {
		if (this.init) {
			return;
		}
		this.init = true;
		this.registerAlloys();
		this.initSetup(BaseMetals.MODID);
	}

	/**
	 *
	 *
	public void postInit() {
		if (this.postInit) {
			return;
		}
		this.postInit = true;
		this.postInitSetup(BaseMetals.MODID);
	}
	
	
	@SubscribeEvent
	public void registerModifiers(final RegistryEvent.Register<Item> event) {
		super.modifierSetup();
		super.modifierRegister();
	}

	private void registerMelting() {
		if (Materials.hasMaterial(MaterialNames.COAL)) {
			registry.registerMelting(net.minecraft.init.Items.COAL,
					FluidRegistry.getFluidStack(MaterialNames.COAL, 144));
		}

		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);
			registry.registerMelting(mercury.getItem(Names.INGOT),
					FluidRegistry.getFluidStack(mercury.getName(), 144));
		}

		if (Materials.hasMaterial(MaterialNames.PRISMARINE)) {
			final MMDMaterial prismarine = Materials.getMaterialByName(MaterialNames.PRISMARINE);
			registry.registerMelting(net.minecraft.init.Items.PRISMARINE_SHARD,
					FluidRegistry.getFluidStack(prismarine.getName(), 666));
		}
	}

	private boolean isTraitLoc(final String loc) {
		switch (loc) {
			case TinkersTraitLocation.BOW:
			case TinkersTraitLocation.BOWSTRING:
			case TinkersTraitLocation.EXTRA:
			case TinkersTraitLocation.FLETCHING:
			case TinkersTraitLocation.HANDLE:
			case TinkersTraitLocation.HEAD:
			case TinkersTraitLocation.PROJECTILE:
			case TinkersTraitLocation.SHAFT:
				return true;
			default:
				return false;
		}
	}

	private void addTraits(final TCMaterial mat, final String[] traits) {
		int i = 0;
		while (i < traits.length) {
			if (i == (traits.length - 1)) {
				// can only be a "general" trait
				mat.addTrait("general", traits[i]);
			} else {
				final String item = traits[i];
				if (this.isTraitLoc(item)) {
					if ((i + 1) >= traits.length) {
						return;
					}
					i++;
					mat.addTrait(item, traits[i]);
				} else {
					mat.addTrait("general", traits[i]);
				}
			}
			i++;
		}
	}

	private void registerMaterial(final String name, final boolean castable,
			final boolean craftable, final String... traits) {
		if (!Materials.hasMaterial(name)) {
			return;
		}

		final MMDMaterial mmdMat = Materials.getMaterialByName(name);
		final TCMaterial mat = registry.newMaterial(name, mmdMat.getTintColor());

		if (mmdMat.isEmpty()) {
			return;
		}

		if (castable) {
			mat.setCastable();
		}
		if (craftable) {
			mat.setCraftable();
		}

		mat.setSourceMaterial(mmdMat);
		mat.genStatsFromSource();

		if (traits.length > 0) {
			this.addTraits(mat, traits);
		}

		mat.settle();
	}

	private void registerAlloys() {
		if (Materials.hasMaterial(MaterialNames.AQUARIUM)
				&& Materials.hasMaterial(MaterialNames.COPPER)
				&& Materials.hasMaterial(MaterialNames.ZINC)) {
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.AQUARIUM, 3);
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 2);
			final FluidStack zinc = FluidRegistry.getFluidStack(MaterialNames.ZINC, 1);
			final FluidStack prismarine = FluidRegistry.getFluidStack(MaterialNames.PRISMARINE, 3);
			registry.registerAlloy(MaterialNames.AQUARIUM, output, copper, zinc, prismarine);
		}

		if (Materials.hasMaterial(MaterialNames.CUPRONICKEL)
				&& Materials.hasMaterial(MaterialNames.COPPER)
				&& Materials.hasMaterial(MaterialNames.NICKEL)) {
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.CUPRONICKEL, 4);
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 3);
			final FluidStack nickel = FluidRegistry.getFluidStack(MaterialNames.NICKEL, 1);
			registry.registerAlloy(MaterialNames.CUPRONICKEL, output, copper, nickel);
		}

		if (Materials.hasMaterial(MaterialNames.INVAR)
				&& Materials.hasMaterial(MaterialNames.NICKEL)) {
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.INVAR, 3);
			final FluidStack iron = FluidRegistry.getFluidStack(MaterialNames.IRON, 2);
			final FluidStack nickel = FluidRegistry.getFluidStack(MaterialNames.NICKEL, 1);
			registry.registerAlloy(MaterialNames.INVAR, output, iron, nickel);
		}

		if (Materials.hasMaterial(MaterialNames.MITHRIL)
				&& Materials.hasMaterial(MaterialNames.COLDIRON)
				&& Materials.hasMaterial(MaterialNames.SILVER)
				&& Materials.hasMaterial(MaterialNames.MERCURY)) {
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.MITHRIL, 3);
			final FluidStack coldiron = FluidRegistry.getFluidStack(MaterialNames.COLDIRON, 1);
			final FluidStack silver = FluidRegistry.getFluidStack(MaterialNames.SILVER, 2);
			final FluidStack mercury = FluidRegistry.getFluidStack(MaterialNames.MERCURY, 1);
			registry.registerAlloy(MaterialNames.MITHRIL, output, coldiron, silver, mercury);
		}

		if (Materials.hasMaterial(MaterialNames.PEWTER) && Materials.hasMaterial(MaterialNames.LEAD)
				&& Materials.hasMaterial(MaterialNames.COPPER)
				&& Materials.hasMaterial(MaterialNames.TIN)) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.PEWTER, 144);
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 2);
			final FluidStack tin = FluidRegistry.getFluidStack(MaterialNames.TIN, 137);
			final FluidStack lead = FluidRegistry.getFluidStack(MaterialNames.LEAD, 5);
			registry.registerAlloy(MaterialNames.PEWTER, output, copper, tin, lead);
		}

		if (Materials.hasMaterial(MaterialNames.STEEL)) {
			final FluidStack output = FluidRegistry.getFluidStack(MaterialNames.STEEL, 8);
			final FluidStack iron = FluidRegistry.getFluidStack(MaterialNames.IRON, 8);
			final FluidStack coal = FluidRegistry.getFluidStack(MaterialNames.COAL, 1);
			registry.registerAlloy(MaterialNames.STEEL, output, iron, coal);
		}
	}
	*/
}
