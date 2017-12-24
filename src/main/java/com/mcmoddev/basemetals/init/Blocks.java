package com.mcmoddev.basemetals.init;

import java.util.Arrays;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.block.BlockHumanDetector;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This class initializes all blocks in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Blocks extends com.mcmoddev.lib.init.Blocks {

	public static Block humanDetector;

	private static boolean initDone = false;

	protected Blocks() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}
		
		com.mcmoddev.basemetals.util.Config.init();
		com.mcmoddev.lib.init.Blocks.init();
		Materials.init();
		ItemGroups.init();

		registerVanilla();

		String[] simpleFullBlocks = new String[] { MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH, MaterialNames.COLDIRON, MaterialNames.COPPER, MaterialNames.LEAD,
				MaterialNames.NICKEL, MaterialNames.PLATINUM, MaterialNames.SILVER, MaterialNames.TIN,
				MaterialNames.ZINC };

		String[] alloyFullBlocks = new String[] { MaterialNames.AQUARIUM, MaterialNames.BRASS, MaterialNames.BRONZE,
				MaterialNames.CUPRONICKEL, MaterialNames.ELECTRUM, MaterialNames.INVAR, MaterialNames.MITHRIL,
				MaterialNames.PEWTER, MaterialNames.STEEL };

		Arrays.stream(simpleFullBlocks)
				.filter(Materials::hasMaterial)
				.forEach(name -> {
					final MMDMaterial material = Materials.getMaterialByName(name);

					create(Names.BLOCK, material);
					create(Names.PLATE, material);
					create(Names.ORE, material);
					create(Names.BARS, material);
					create(Names.DOOR, material);
					create(Names.TRAPDOOR, material);

					create(Names.BUTTON, material);
					create(Names.SLAB, material);
					create(Names.DOUBLE_SLAB, material);
					create(Names.LEVER, material);
					create(Names.PRESSURE_PLATE, material);
					create(Names.STAIRS, material);
					create(Names.WALL, material);
				});

		Arrays.stream(alloyFullBlocks)
				.filter(Materials::hasMaterial)
				.forEach(name -> {
					final MMDMaterial material = Materials.getMaterialByName(name);

					create(Names.BLOCK, material);
					create(Names.PLATE, material);
					create(Names.ORE, material);
					create(Names.BARS, material);
					create(Names.DOOR, material);
					create(Names.TRAPDOOR, material);

					create(Names.BUTTON, material);
					create(Names.SLAB, material);
					create(Names.DOUBLE_SLAB, material);
					create(Names.LEVER, material);
					create(Names.PRESSURE_PLATE, material);
					create(Names.STAIRS, material);
					create(Names.WALL, material);
				});

		createStarSteel();
		createMercury();

		humanDetector = addBlock(new BlockHumanDetector(), "human_detector", BLOCKS_TAB);

		initDone = true;
	}

	private static void createStarSteel() {
		if (Materials.hasMaterial(MaterialNames.STARSTEEL)) {
			final MMDMaterial starsteel = Materials.getMaterialByName(MaterialNames.STARSTEEL);

			create(Names.BLOCK, starsteel);
			create(Names.PLATE, starsteel);
			create(Names.ORE, starsteel);
			create(Names.BARS, starsteel);
			create(Names.DOOR, starsteel);
			create(Names.TRAPDOOR, starsteel);

			create(Names.BUTTON, starsteel);
			create(Names.SLAB, starsteel);
			create(Names.DOUBLE_SLAB, starsteel);
			create(Names.LEVER, starsteel);
			create(Names.PRESSURE_PLATE, starsteel);
			create(Names.STAIRS, starsteel);
			create(Names.WALL, starsteel);

			if (starsteel.hasBlock(Names.BLOCK))
				starsteel.getBlock(Names.BLOCK).setLightLevel(0.5f);

			if (starsteel.hasBlock(Names.PLATE))
				starsteel.getBlock(Names.PLATE).setLightLevel(0.5f);

			if (starsteel.hasBlock(Names.ORE))
				starsteel.getBlock(Names.ORE).setLightLevel(0.5f);

			if (starsteel.hasBlock(Names.BARS))
				starsteel.getBlock(Names.BARS).setLightLevel(0.5f);

			if (starsteel.hasBlock(Names.DOOR))
				starsteel.getBlock(Names.DOOR).setLightLevel(0.5f);

			if (starsteel.hasBlock(Names.TRAPDOOR))
				starsteel.getBlock(Names.TRAPDOOR).setLightLevel(0.5f);
		}
	}

	private static void createMercury() {
		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);
			create(Names.ORE, mercury);
			if (mercury.hasBlock(Names.ORE))
				mercury.getBlock(Names.ORE).setHardness(3.0f).setResistance(5.0f);
		}
	}

	protected static void registerVanilla() {
		// Vanilla Materials get their Ore and Block always
		final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);
		final MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);
		final MMDMaterial diamond = Materials.getMaterialByName(MaterialNames.DIAMOND);
		final MMDMaterial emerald = Materials.getMaterialByName(MaterialNames.EMERALD);
		final MMDMaterial gold = Materials.getMaterialByName(MaterialNames.GOLD);
		final MMDMaterial iron = Materials.getMaterialByName(MaterialNames.IRON);
		final MMDMaterial lapis = Materials.getMaterialByName(MaterialNames.LAPIS);
		final MMDMaterial obsidian = Materials.getMaterialByName(MaterialNames.OBSIDIAN);
		final MMDMaterial quartz = Materials.getMaterialByName(MaterialNames.QUARTZ);
		final MMDMaterial redstone = Materials.getMaterialByName(MaterialNames.REDSTONE);

		coal.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.COAL_BLOCK);
		coal.addNewBlock(Names.ORE, net.minecraft.init.Blocks.COAL_ORE);

		diamond.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.DIAMOND_BLOCK);
		diamond.addNewBlock(Names.ORE, net.minecraft.init.Blocks.DIAMOND_ORE);

		emerald.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.EMERALD_BLOCK);
		emerald.addNewBlock(Names.ORE, net.minecraft.init.Blocks.EMERALD_ORE);

		gold.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.GOLD_BLOCK);
		gold.addNewBlock(Names.ORE, net.minecraft.init.Blocks.GOLD_ORE);
		gold.addNewBlock(Names.PRESSURE_PLATE, net.minecraft.init.Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);

		iron.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.IRON_BLOCK);
		iron.addNewBlock(Names.ORE, net.minecraft.init.Blocks.IRON_ORE);
		iron.addNewBlock(Names.BARS, net.minecraft.init.Blocks.IRON_BARS);
		iron.addNewBlock(Names.DOOR, net.minecraft.init.Blocks.IRON_DOOR);
		iron.addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.IRON_TRAPDOOR);
		iron.addNewBlock(Names.PRESSURE_PLATE, net.minecraft.init.Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);

		lapis.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.LAPIS_BLOCK);
		lapis.addNewBlock(Names.ORE, net.minecraft.init.Blocks.LAPIS_ORE);

		obsidian.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.OBSIDIAN);

		quartz.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.QUARTZ_BLOCK);
		quartz.addNewBlock(Names.ORE, net.minecraft.init.Blocks.QUARTZ_ORE);
		quartz.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.QUARTZ_STAIRS);

		redstone.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.REDSTONE_BLOCK);
		redstone.addNewBlock(Names.ORE, net.minecraft.init.Blocks.REDSTONE_ORE);

		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			create(Names.BLOCK, charcoal);
		}

		if (Materials.hasMaterial(MaterialNames.DIAMOND)) {
			create(Names.BARS, diamond);
			create(Names.DOOR, diamond);
			create(Names.TRAPDOOR, diamond);

			create(Names.BUTTON, diamond);
			create(Names.SLAB, diamond);
			create(Names.DOUBLE_SLAB, diamond);
			create(Names.LEVER, diamond);
			create(Names.PRESSURE_PLATE, diamond);
			create(Names.STAIRS, diamond);
			create(Names.WALL, diamond);
		}

		if (Materials.hasMaterial(MaterialNames.EMERALD)) {
			create(Names.BARS, emerald);
			create(Names.DOOR, emerald);
			create(Names.TRAPDOOR, emerald);

			create(Names.BUTTON, emerald);
			create(Names.SLAB, emerald);
			create(Names.DOUBLE_SLAB, emerald);
			create(Names.LEVER, emerald);
			create(Names.PRESSURE_PLATE, emerald);
			create(Names.STAIRS, emerald);
			create(Names.WALL, emerald);
		}

		if (Materials.hasMaterial(MaterialNames.GOLD)) {
			create(Names.PLATE, gold);
			create(Names.BARS, gold);
			create(Names.DOOR, gold);
			create(Names.TRAPDOOR, gold);

			create(Names.BUTTON, gold);
			create(Names.SLAB, gold);
			create(Names.DOUBLE_SLAB, gold);
			create(Names.LEVER, gold);
			create(Names.PRESSURE_PLATE, gold);
			create(Names.STAIRS, gold);
			create(Names.WALL, gold);
		}

		if (Materials.hasMaterial(MaterialNames.IRON)) {
			create(Names.PLATE, iron);

			create(Names.BUTTON, iron);
			create(Names.SLAB, iron);
			create(Names.DOUBLE_SLAB, iron);
			create(Names.LEVER, iron);
			create(Names.PRESSURE_PLATE, iron);
			create(Names.STAIRS, iron);
			create(Names.WALL, iron);
		}

		if (Materials.hasMaterial(MaterialNames.OBSIDIAN)) {
			create(Names.BARS, obsidian);
			create(Names.DOOR, obsidian);
			create(Names.TRAPDOOR, obsidian);

			create(Names.BUTTON, obsidian);
			create(Names.SLAB, obsidian);
			create(Names.DOUBLE_SLAB, obsidian);
			create(Names.LEVER, obsidian);
			create(Names.PRESSURE_PLATE, obsidian);
			create(Names.STAIRS, obsidian);
			create(Names.WALL, obsidian);
		}

		if (Materials.hasMaterial(MaterialNames.QUARTZ)) {
			create(Names.BARS, quartz);
			create(Names.DOOR, quartz);
			create(Names.TRAPDOOR, quartz);

			create(Names.BUTTON, quartz);
			create(Names.LEVER, quartz);
			create(Names.PRESSURE_PLATE, quartz);
			create(Names.WALL, quartz);
		}

		if (Materials.hasMaterial(MaterialNames.STONE)) {
			// Stub
		}

		if (Materials.hasMaterial(MaterialNames.WOOD)) {
			// Stub
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		for (MMDMaterial mat : Materials.getMaterialsByMod(BaseMetals.MODID)) {
            for (Block block : mat.getBlocks()) {
            	if (block.getRegistryName().getResourceDomain().equals(BaseMetals.MODID)) {
            		event.getRegistry().register(block);
            	}
            }			
		}

		if( humanDetector != null ) {
			event.getRegistry().register(humanDetector);
		}
	}

	protected static Block create(@Nonnull final Names name, @Nonnull final MMDMaterial material) {
		return create(name, material, BLOCKS_TAB);
	}
}
