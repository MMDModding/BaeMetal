package com.mcmoddev.lib.integration.plugins;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.integration.plugins.TinkersModifierRegistryEvent;
import com.mcmoddev.basemetals.integration.plugins.TinkersTraitRegistryEvent;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.IntegrationInitEvent;
import com.mcmoddev.lib.integration.IntegrationPostInitEvent;
import com.mcmoddev.lib.integration.IntegrationPreInitEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerMaterial.TinkersStatTypes;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerMaterial.TinkersTraitLocation;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.ModifierFakeDiamond;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.ModifierLeadPlated;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.ModifierToxic;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitBrittle;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitHeavy;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitRadioactive;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitSoft;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitSparkly;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitToxic;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.traits.ITrait;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * TiC Plugin, redesigned.
 *
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 * @since 2018-04-26
 */
public class TinkersConstructBase implements IIntegration {
	public static final String PLUGIN_MODID = "tconstruct";
	private static final IForgeRegistry<TinkerMaterial> registry = new RegistryBuilder<TinkerMaterial>()
			.disableSaving()
			.setMaxID( 65535 )
			.setName(new ResourceLocation("mmdlib", "tinker_registry"))
			.setType(TinkerMaterial.class)
			.create();

	private static final Map<String, AlloyRecipe> alloys = new HashMap<>();
	private static final Map<String, ITrait> traits = new HashMap<>();
	private static final Map<String, Modifier> modifiers = new HashMap<>();
	private static final List<Pair<ItemStack, FluidStack>> extraMeltings = new LinkedList<>();
	private static final List<MaterialIntegration> integrations = new LinkedList<>();

	public TinkersConstructBase() {
		BaseMetals.logger.fatal("TinkersConstructBase constructor");
		
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		registerTraits();
		MinecraftForge.EVENT_BUS.post(new TinkersTraitRegistryEvent());
		registerModifiers();
		MinecraftForge.EVENT_BUS.post(new TinkersModifierRegistryEvent());
	}

	@SubscribeEvent
	public void registerModifiers(final RegistryEvent.Register<Item> event) {
		traits.entrySet().stream()
		.map(te -> te.getValue())
		.filter(trait -> TinkerRegistry.getTrait(trait.getIdentifier()) == null)
		.forEach(trait -> TinkerRegistry.addTrait(trait));
		
		modifiers.entrySet().stream()
		.map(me -> me.getValue())
		.filter(mod -> TinkerRegistry.getModifier(mod.identifier) == null)
		.forEach(modifier -> TinkerRegistry.registerModifier(modifier));
	}


	private void registerTraits() {
		registerTrait("brittle", new TraitBrittle());
		registerTrait("heavy", new TraitHeavy());
		registerTrait("radioactive", new TraitRadioactive());
		registerTrait("soft", new TraitSoft());
		registerTrait("sparkly", new TraitSparkly());
		registerTrait("toxic", new TraitToxic());
	}

	private void registerModifiers() {
		registerModifier("fake diamond", new ModifierFakeDiamond());
		registerModifier("lead plated", new ModifierLeadPlated());
		registerModifier("toxic", new ModifierToxic());
	}

	public TinkerMaterial newMaterial(@Nonnull MMDMaterial material) {
		return new TinkerMaterial(material);
	}

	public void registerMaterial(@Nonnull TinkerMaterial material) {
		String activeMod = Loader.instance().activeModContainer().getModId();
		ResourceLocation name = new ResourceLocation(activeMod, material.getName().toLowerCase());
		if(material.getRegistryName()==null)
			material.setRegistryName(name);
		registry.register(material);
	}

	public TinkerMaterial getMaterial(@Nonnull String name) {
		if (name.matches(":")) {
			return this.getMaterial(new ResourceLocation(name));
		}

		return this.getMaterial(Loader.instance().activeModContainer().getModId(), name);
	}

	public TinkerMaterial getMaterial(@Nonnull String modId, @Nonnull String name) {
		return this.getMaterial(new ResourceLocation(modId, name));
	}

	public TinkerMaterial getMaterial(@Nonnull ResourceLocation key) {
		return registry.getValue(key);
	}

	public void addExtraMelting(@Nonnull String materialName, @Nonnull String name, int amount) {
		this.addExtraMelting(this.getMaterial(materialName), name, amount);
	}

	public void addExtraMelting(@Nonnull String modId, @Nonnull String materialName, @Nonnull String name, int amount) {
		this.addExtraMelting(this.getMaterial(modId, materialName), name, amount);
	}

	public void addExtraMelting(@Nonnull ResourceLocation loc, @Nonnull String name, int amount) {
		this.addExtraMelting(this.getMaterial(loc), name, amount);
	}

	public void addExtraMelting(TinkerMaterial material, @Nonnull String name, int amount) {
		if(material == null) return;

		material.addExtraMelting(name, amount);
	}

	public void registerMaterial(@Nonnull String materialName, boolean craftable, boolean castable, Object...traits) {
		registerMaterial(materialName, craftable, castable, true, traits);
	}

	public void registerMaterial(@Nonnull String materialName, boolean craftable, boolean castable,
			boolean toolForge, Object...traitsList) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		TinkerMaterial mat = newMaterial(material);
		mat.setCastable(castable);
		mat.setCraftable(craftable);
		mat.setToolForge(toolForge);

		int i = 0;
		while (i < traitsList.length) {
			if (traitsList[i] instanceof TinkerMaterial.TinkersTraitLocation) {
				mat.addTrait((String)traitsList[i+1], (TinkerMaterial.TinkersTraitLocation)traitsList[i]);
				i++;
			} else {
				mat.addTrait((String)traitsList[i], TinkerMaterial.TinkersTraitLocation.GENERAL);
			}
			i++;
		}

		mat.setRegistryName(materialName);
		registerMaterial(mat);
	}

	public void registerMaterial(@Nonnull String materialName, boolean craftable, boolean castable, boolean toolForge) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		TinkerMaterial mat = newMaterial(material);
		mat.setCastable(castable);
		mat.setCraftable(craftable);
		mat.setToolForge(toolForge);

		mat.setRegistryName(materialName);
		mat.settle();
		registerMaterial(mat);		
	}

	public void registerAlloy(@Nonnull String materialName, FluidStack result, FluidStack...recipe) {
		alloys.put(materialName, new AlloyRecipe(result, recipe));
	}

	public void registerTrait(String traitName, ITrait trait) {
		traits.put(traitName, trait);
	}

	public void registerModifier(String modifierName, Modifier modifier) {
		modifiers.put(modifierName, modifier);
	}

	public void registerMelting(@Nonnull ItemStack item, @Nonnull FluidStack fluid) {
		extraMeltings.add(Pair.of(item, fluid));
	}


	@SubscribeEvent
	public void preInit(IntegrationPreInitEvent ev) {
		setupIntegrations();
		addMaterialStats();
		ensureMaterialsVisible();
	}

	@SubscribeEvent
	public void init(IntegrationInitEvent ev) {
		registerExtraMeltings();
		addItemsToMaterials();
		addTraits();
		ensureMaterialsVisible();
	}

	private void addItemsToMaterials() {
		registry.getValues().stream()
		.forEach( tm -> {
			tm.getTinkerMaterial().addCommonItems(tm.getMMDMaterial().getCapitalizedName());
			tm.getTinkerMaterial().addItemIngot(Oredicts.INGOT+tm.getMMDMaterial().getCapitalizedName());
			tm.getTinkerMaterial().addItem(tm.getMMDMaterial().getItemStack(Names.INGOT), 1, Material.VALUE_Ingot);
			tm.getTinkerMaterial().addItem(tm.getMMDMaterial().getBlockItemStack(Names.BLOCK), 1, Material.VALUE_Block);
			tm.getTinkerMaterial().addItem(tm.getMMDMaterial().getItemStack(Names.SHARD), 1, Material.VALUE_Shard);
			tm.getTinkerMaterial().addItem(tm.getMMDMaterial().getBlockItemStack(Names.ORE), 1, Material.VALUE_Ingot*2);
			
			ItemStack represents = null;
			
			switch(tm.getMMDMaterial().getType()) {
			case GEM:
				represents = tm.getMMDMaterial().getItemStack(Names.GEM);
				break;
			case CRYSTAL:
			case MINERAL:
			case METAL:
				represents = tm.getMMDMaterial().getItemStack(Names.INGOT);
				break;
			case ROCK:
			case WOOD:
				represents = tm.getMMDMaterial().getBlockItemStack(Names.BLOCK);
				break;
			}
			
			if(represents != null) tm.getTinkerMaterial().setRepresentativeItem(represents);
		});
	}

	@SubscribeEvent
	public void postInit(IntegrationPostInitEvent ev) {
		integrations.stream().forEach( mi -> mi.integrate() );
		registerAlloyRecipes();
		ensureMaterialsVisible();
	}

	private void setupIntegrations() {
		for (TinkerMaterial mat : registry.getValues()) {
			if (mat.getMMDMaterial().equals(Materials.EMPTY) || mat.getName().toLowerCase().equals("empty")) {
				continue;
			}
			
			mat.settle();

			Material m = mat.getTinkerMaterial();

			final MaterialIntegration integration = new MaterialIntegration(m, mat.getMMDMaterial().getFluid());

			if (mat.getToolForge()) {
				integration.toolforge();
			}

			integrations.add(integration);
			TinkerRegistry.integrate(integration);
			integration.preInit();
		}
	}

	private void addMaterialStats() {
		for (final TinkerMaterial mat : registry.getValues()) {
			mat.settle(); // be double sure!
			final HeadMaterialStats headStats = (HeadMaterialStats) mat.getStat(TinkersStatTypes.HEAD);
			final HandleMaterialStats handleStats = (HandleMaterialStats) mat.getStat(TinkersStatTypes.HANDLE);
			final ExtraMaterialStats extraStats = (ExtraMaterialStats) mat.getStat(TinkersStatTypes.EXTRA);
			final BowMaterialStats bowStats = (BowMaterialStats) mat.getStat(TinkersStatTypes.BOW);
			final ArrowShaftMaterialStats arrowStats = (ArrowShaftMaterialStats) mat.getStat(TinkersStatTypes.ARROWSHAFT);
			final FletchingMaterialStats fletchingStats = (FletchingMaterialStats) mat.getStat(TinkersStatTypes.FLETCHING);

			final Material work = mat.getTinkerMaterial();
			TinkerRegistry.addMaterialStats(work, headStats, handleStats, extraStats);
			TinkerRegistry.addMaterialStats(work, bowStats);
			TinkerRegistry.addMaterialStats(work, arrowStats);
			TinkerRegistry.addMaterialStats(work, fletchingStats);
		}
	}
	
	private void ensureMaterialsVisible() {
		registry.getValues().stream().forEach( mat -> mat.getTinkerMaterial().setVisible());
	}
	
	private void registerExtraMeltings() {
		extraMeltings.stream().forEach( p -> TinkerRegistry.registerMelting(new MeltingRecipe(RecipeMatch.of(p.getLeft()), p.getRight())));
	}
	
	private void addTraits() {		
		registry.getValues().stream()
		.forEach( tm -> {
			for(TinkersTraitLocation loc : TinkersTraitLocation.values()) {
				if (tm.getTraits(loc).size() > 0) {
					if (loc == TinkersTraitLocation.GENERAL) {
						tm.getTraits(loc).stream()
						.forEach( t -> { 
							ITrait tr = null;
							if (t != null) {
								tr = TinkerRegistry.getTrait(t);
							}
							
							if (tr == null) {
								tr = TinkerRegistry.getTrait("mmd-"+t);
							}
							if (tr != null) {
								tm.getTinkerMaterial().addTrait(tr);
							}
						});
					} else {
						tm.getTraits(loc).stream()
						.forEach( t -> { 
							ITrait tr = null;
							if (t != null) {
								tr = TinkerRegistry.getTrait(t);
							}
							
							if (tr == null) {
								tr = TinkerRegistry.getTrait("mmd-"+t);
							}
							if (tr != null) {
								tm.getTinkerMaterial().addTrait(tr, loc.toString());
							}
						});
					}
				}
			}
		});
	}
	
	private void registerAlloyRecipes() {
		alloys.entrySet().stream()
		.forEach(e -> TinkerRegistry.registerAlloy(e.getValue()));
	}
}
