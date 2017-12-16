package com.mcmoddev.basemetals.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.item.ItemMMDNugget;
import com.mcmoddev.lib.item.ItemMMDPowder;
import com.mcmoddev.lib.item.ItemMMDSmallPowder;
import com.mcmoddev.lib.item.ItemMMDBlock;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.interfaces.ITabProvider;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.BMeIoC;
import com.mcmoddev.lib.util.Oredicts;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This class initializes all items in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	private static boolean initDone = false;
	protected Items() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		// IoC resolutions here
		BMeIoC IoC = BMeIoC.getInstance();
		IoC.resolve(ITabProvider.class);
		
		com.mcmoddev.basemetals.util.Config.init();
		Blocks.init();
		com.mcmoddev.lib.init.Items.init();

		// create and register vanilla stuffs
		Map<String,MMDMaterial> vanillaMats = new HashMap<>();

		List<String> vanillaMatNames = Arrays.asList( MaterialNames.CHARCOAL, MaterialNames.COAL, MaterialNames.DIAMOND, 
				MaterialNames.EMERALD, MaterialNames.GOLD, MaterialNames.IRON, MaterialNames.LAPIS, MaterialNames.OBSIDIAN,
				MaterialNames.QUARTZ, MaterialNames.REDSTONE, MaterialNames.STONE, MaterialNames.WOOD );

		vanillaMatNames.forEach( name -> vanillaMats.put(name,  Materials.getMaterialByName(name)) );

		Materials.getMaterialByName(MaterialNames.CHARCOAL).addNewItem(Names.INGOT, new ItemStack(net.minecraft.init.Items.COAL, 1, 1).getItem());
		Materials.getMaterialByName(MaterialNames.COAL).addNewItem(Names.INGOT, new ItemStack(net.minecraft.init.Items.COAL, 1, 0).getItem());

		addDiamondBits();
		addGoldBits();
		addIronBits();
		addStoneBits();
		addWoodBits();

		Materials.getMaterialByName(MaterialNames.EMERALD).addNewItem(Names.INGOT, net.minecraft.init.Items.EMERALD);
		Materials.getMaterialByName(MaterialNames.LAPIS).addNewItem(Names.POWDER, new ItemStack(net.minecraft.init.Items.DYE, 1, 4).getItem());
		Materials.getMaterialByName(MaterialNames.QUARTZ).addNewItem(Names.INGOT, net.minecraft.init.Items.QUARTZ);
		Materials.getMaterialByName(MaterialNames.REDSTONE).addNewItem(Names.POWDER, net.minecraft.init.Items.REDSTONE);

		doSpecialMats();

		// create and register modded stuffs
		List<String> matsModSupport = Arrays.asList( MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY, MaterialNames.BISMUTH,
				MaterialNames.COLDIRON, MaterialNames.PLATINUM, MaterialNames.NICKEL, MaterialNames.STARSTEEL, MaterialNames.ZINC );

		List<String> myModMats = Arrays.asList( MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY, MaterialNames.AQUARIUM,
				MaterialNames.BISMUTH, MaterialNames.BRASS, MaterialNames.BRONZE, MaterialNames.COLDIRON, MaterialNames.COPPER,
				MaterialNames.CUPRONICKEL, MaterialNames.EMERALD, MaterialNames.ELECTRUM, MaterialNames.INVAR, MaterialNames.LEAD,
				MaterialNames.OBSIDIAN, MaterialNames.MITHRIL, MaterialNames.NICKEL, MaterialNames.PEWTER, MaterialNames.PLATINUM, 
				MaterialNames.QUARTZ, MaterialNames.SILVER, MaterialNames.STARSTEEL, MaterialNames.STEEL, MaterialNames.TIN,
				MaterialNames.ZINC);

		myModMats.stream()
			.filter(Materials::hasMaterial)
			.filter(name -> !Materials.getMaterialByName(name).equals(Materials.emptyMaterial))
			.forEach(name -> createItemsFull(Materials.getMaterialByName(name)));

		matsModSupport.stream()
			.filter(Materials::hasMaterial)
			.filter(name -> !Materials.getMaterialByName(name).equals(Materials.emptyMaterial))
			.forEach(name -> createItemsModSupport(Materials.getMaterialByName(name)));

		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);

			create(Names.INGOT, mercury, ITEMS_TAB);
			create(Names.NUGGET, mercury, ITEMS_TAB);
			create(Names.POWDER, mercury, ITEMS_TAB);
			create(Names.SMALLPOWDER, mercury, ITEMS_TAB);
		}

		addToMetList();

		initDone = true;
	}

	private static void doSpecialMats() {
		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);

			create(Names.NUGGET, charcoal, ITEMS_TAB);
			create(Names.POWDER, charcoal, ITEMS_TAB);
			create(Names.SMALLPOWDER, charcoal, ITEMS_TAB);

			if (charcoal.hasItem(Names.NUGGET))
				((ItemMMDNugget)charcoal.getItem(Names.NUGGET)).setBurnTime(200);

			if (charcoal.hasItem(Names.POWDER))
				((ItemMMDPowder)charcoal.getItem(Names.POWDER)).setBurnTime(1600);

			if (charcoal.hasItem(Names.SMALLPOWDER))
				((ItemMMDSmallPowder)charcoal.getItem(Names.SMALLPOWDER)).setBurnTime(200);

			if (charcoal.hasBlock(Names.BLOCK))
				((ItemMMDBlock)charcoal.getItem("ItemBlock_charcoal_block")).setBurnTime(16000);
		}

		if (Materials.hasMaterial(MaterialNames.COAL)) {
			final MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);

			create(Names.NUGGET, coal, ITEMS_TAB);
			create(Names.POWDER, coal, ITEMS_TAB);
			create(Names.SMALLPOWDER, coal, ITEMS_TAB);

			if (coal.hasItem(Names.NUGGET))
				((ItemMMDNugget)coal.getItem(Names.NUGGET)).setBurnTime(200);

			if (coal.hasItem(Names.POWDER))
				((ItemMMDPowder)coal.getItem(Names.POWDER)).setBurnTime(1600);

			if (coal.hasItem(Names.SMALLPOWDER))
				((ItemMMDSmallPowder)coal.getItem(Names.SMALLPOWDER)).setBurnTime(200);
		}

		if (Materials.hasMaterial(MaterialNames.REDSTONE)) {
			final MMDMaterial redstone = Materials.getMaterialByName(MaterialNames.REDSTONE);

			create(Names.INGOT, redstone, ITEMS_TAB);
			create(Names.SMALLPOWDER, redstone, ITEMS_TAB);
		}
		
		if (Materials.hasMaterial(MaterialNames.LAPIS)) {
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.LAPIS), ITEMS_TAB);
		}
	}

	private static void addDiamondBits() {
		final MMDMaterial diamond = Materials.getMaterialByName(MaterialNames.DIAMOND);

		diamond.addNewItem(Names.AXE, net.minecraft.init.Items.DIAMOND_AXE);
		diamond.addNewItem(Names.HOE, net.minecraft.init.Items.DIAMOND_HOE);
		diamond.addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.DIAMOND_HORSE_ARMOR);
		diamond.addNewItem(Names.PICKAXE, net.minecraft.init.Items.DIAMOND_PICKAXE);
		diamond.addNewItem(Names.SHOVEL, net.minecraft.init.Items.DIAMOND_SHOVEL);
		diamond.addNewItem(Names.SWORD, net.minecraft.init.Items.DIAMOND_SWORD);
		diamond.addNewItem(Names.BOOTS, net.minecraft.init.Items.DIAMOND_BOOTS);
		diamond.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.DIAMOND_CHESTPLATE);
		diamond.addNewItem(Names.HELMET, net.minecraft.init.Items.DIAMOND_HELMET);
		diamond.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.DIAMOND_LEGGINGS);
		diamond.addNewItem(Names.INGOT, net.minecraft.init.Items.DIAMOND);

		if (Materials.hasMaterial(MaterialNames.DIAMOND)) {
			createItemsFull(diamond);
		}
	}

	private static void addGoldBits() {
		final MMDMaterial gold = Materials.getMaterialByName(MaterialNames.GOLD);

		gold.addNewItem(Names.AXE, net.minecraft.init.Items.GOLDEN_AXE);
		gold.addNewItem(Names.HOE, net.minecraft.init.Items.GOLDEN_HOE);
		gold.addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.GOLDEN_HORSE_ARMOR);
		gold.addNewItem(Names.PICKAXE, net.minecraft.init.Items.GOLDEN_PICKAXE);
		gold.addNewItem(Names.SHOVEL, net.minecraft.init.Items.GOLDEN_SHOVEL);
		gold.addNewItem(Names.SWORD, net.minecraft.init.Items.GOLDEN_SWORD);
		gold.addNewItem(Names.BOOTS, net.minecraft.init.Items.GOLDEN_BOOTS);
		gold.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.GOLDEN_CHESTPLATE);
		gold.addNewItem(Names.HELMET, net.minecraft.init.Items.GOLDEN_HELMET);
		gold.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.GOLDEN_LEGGINGS);
		gold.addNewItem(Names.INGOT, net.minecraft.init.Items.GOLD_INGOT);
		gold.addNewItem(Names.NUGGET, net.minecraft.init.Items.GOLD_NUGGET);

		if (Materials.hasMaterial(MaterialNames.GOLD)) {
			createItemsFull(gold);
		}
	}

	private static void addIronBits() {
		final MMDMaterial iron = Materials.getMaterialByName(MaterialNames.IRON);

		iron.addNewItem(Names.AXE, net.minecraft.init.Items.IRON_AXE);
		iron.addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
		iron.addNewItem(Names.HOE, net.minecraft.init.Items.IRON_HOE);
		iron.addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.IRON_HORSE_ARMOR);
		iron.addNewItem(Names.PICKAXE, net.minecraft.init.Items.IRON_PICKAXE);
		iron.addNewItem(Names.SHOVEL, net.minecraft.init.Items.IRON_SHOVEL);
		iron.addNewItem(Names.SWORD, net.minecraft.init.Items.IRON_SWORD);
		iron.addNewItem(Names.BOOTS, net.minecraft.init.Items.IRON_BOOTS);
		iron.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.IRON_CHESTPLATE);
		iron.addNewItem(Names.HELMET, net.minecraft.init.Items.IRON_HELMET);
		iron.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.IRON_LEGGINGS);
		iron.addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
		iron.addNewItem(Names.INGOT, net.minecraft.init.Items.IRON_INGOT);
		iron.addNewItem(Names.NUGGET, net.minecraft.init.Items.IRON_NUGGET);
		iron.addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);

		if (Materials.hasMaterial(MaterialNames.IRON)) {
			createItemsFull(iron);
		}
	}

	private static void addStoneBits() {
		final MMDMaterial stone = Materials.getMaterialByName(MaterialNames.STONE);

		stone.addNewItem(Names.AXE, net.minecraft.init.Items.STONE_AXE);
		stone.addNewItem(Names.HOE, net.minecraft.init.Items.STONE_HOE);
		stone.addNewItem(Names.PICKAXE, net.minecraft.init.Items.STONE_PICKAXE);
		stone.addNewItem(Names.SHOVEL, net.minecraft.init.Items.STONE_SHOVEL);
		stone.addNewItem(Names.SWORD, net.minecraft.init.Items.STONE_SWORD);
		stone.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.STONE);
		stone.addNewBlock(Names.SLAB, net.minecraft.init.Blocks.STONE_SLAB);
		stone.addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_STONE_SLAB);
		stone.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.STONE_STAIRS);

		if (Materials.hasMaterial(MaterialNames.STONE)) {
			create(Names.CRACKHAMMER, stone, TOOLS_TAB);
			create(Names.ROD, stone, ITEMS_TAB);
			create(Names.GEAR, stone, ITEMS_TAB);
		}
	}

	private static void addWoodBits() {
		final MMDMaterial wood = Materials.getMaterialByName(MaterialNames.WOOD);

		wood.addNewItem(Names.AXE, net.minecraft.init.Items.WOODEN_AXE);
		wood.addNewItem(Names.DOOR, net.minecraft.init.Items.OAK_DOOR);
		wood.addNewItem(Names.HOE, net.minecraft.init.Items.WOODEN_HOE);
		wood.addNewItem(Names.PICKAXE, net.minecraft.init.Items.WOODEN_PICKAXE);
		wood.addNewItem(Names.SHOVEL, net.minecraft.init.Items.WOODEN_SHOVEL);
		wood.addNewItem(Names.SWORD, net.minecraft.init.Items.WOODEN_SWORD);
		wood.addNewBlock(Names.DOOR, net.minecraft.init.Blocks.OAK_DOOR);
		wood.addNewBlock(Names.ORE, net.minecraft.init.Blocks.LOG);
		wood.addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.TRAPDOOR);
		wood.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.PLANKS);
		wood.addNewBlock(Names.SLAB, net.minecraft.init.Blocks.WOODEN_SLAB);
		wood.addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_WOODEN_SLAB);
		wood.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.OAK_STAIRS);
		wood.addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);

		if (Materials.hasMaterial(MaterialNames.WOOD)) {
			create(Names.CRACKHAMMER, wood, TOOLS_TAB);
			create(Names.GEAR, wood, ITEMS_TAB);
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		for (MMDMaterial mat : Materials.getMaterialsByMod(BaseMetals.MODID)) {
			for (Item item : mat.getItems()) {
				if (item.getRegistryName().getResourceDomain().equals(BaseMetals.MODID)) {
					event.getRegistry().register(item);
				}
			}
		}

		if (Blocks.humanDetector != null) {
			final ItemBlock itemBlock = new ItemBlock(Blocks.humanDetector);

			itemBlock.setRegistryName("human_detector");
			itemBlock.setUnlocalizedName(Blocks.humanDetector.getRegistryName().getResourceDomain() + ".human_detector");
			event.getRegistry().register(itemBlock);
		}

		Oredicts.registerItemOreDictionaryEntries();
		Oredicts.registerBlockOreDictionaryEntries();
	}
}
