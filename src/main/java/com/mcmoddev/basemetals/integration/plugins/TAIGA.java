package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.ConfigBase.Options;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = TAIGA.PLUGIN_MODID)
public class TAIGA extends com.mcmoddev.lib.integration.plugins.TAIGABase implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID) && (!Options.isModEnabled(TinkersConstruct.PLUGIN_MODID))) {
			return;
		}

		// TODO: Something in here crashes
		//TraitRegistry.initTAIGATraits();
		//TAIGAMaterials.init();
		//TAIGAItems.init(TAIGAMaterials.materials);

		// TraitRegistry.dumpRegistry();
	}

	/*
	private static class TAIGAMaterials extends com.mcmoddev.lib.init.Materials {

		private static final Field[] allBlocks = Blocks.class.getDeclaredFields();

		private static final List<MMDMaterial> materials = new ArrayList<>();

		public static void init() {
			try {
				for (final Field f : allBlocks) {
					final String t = f.getName();

					if (t.endsWith("Block") && !("basaltBlock".equals(t))) {
						final String name = t.substring(0, t.length() - 5);

						final Block k = (Block) f.get(f.getClass());
						final float harvestLevel = k.getHarvestLevel(null);
						@SuppressWarnings("deprecation")
						final float resist = k.getExplosionResistance(null);

						final MMDMaterial repThis = createOrelessMaterial(name, MaterialType.METAL, harvestLevel * 3.0f, resist / 2.5f, 1.0f, 0x00000000);

						repThis.addNewBlock(Names.BLOCK, k);
						repThis.addNewItem(Names.INGOT, new ItemStack((Item) Items.class.getField(name.toLowerCase() + "Ingot").get(Items.class), 1).getItem());
						repThis.addNewItem(Names.POWDER, new ItemStack((Item) Items.class.getField(name.toLowerCase() + "Dust").get(Items.class), 1).getItem());
						repThis.addNewItem(Names.NUGGET, new ItemStack((Item) Items.class.getField(name.toLowerCase() + "Nugget").get(Items.class), 1).getItem());

						materials.add(repThis);
					}
				}
			} catch (final Exception ex) {
				throw new Error(ex.getMessage());
				// do nought
			}
		}
	}
	*/
}