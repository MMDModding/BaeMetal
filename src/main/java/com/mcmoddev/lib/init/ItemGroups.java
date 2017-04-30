package com.mcmoddev.lib.init;

import net.minecraft.item.*;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcmoddev.lib.init.Items;

/**
 * This class initializes all item groups in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemGroups {

	public static final java.util.function.BiFunction<ItemStack, ItemStack, Integer> sortingAlgorithm = (ItemStack a, ItemStack b) -> {
		final int delta = Items.getSortingValue(a) - Items.getSortingValue(b);
		if (delta == 0) {
			return a.getItem().getUnlocalizedName().compareToIgnoreCase(b.getItem().getUnlocalizedName());
		}
		return delta;
	};

	private static final Map<String, List<MMDCreativeTab>> itemGroupsByModID = new HashMap<>();

	private static boolean initDone = false;

	
	protected ItemGroups() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		initDone = true;
	}

	protected static int addTab(String name, boolean searchable ) {
		String modName = Loader.instance().activeModContainer().getModId();
		String internalTabName = String.format("%s.%s", modName, name);
		MMDCreativeTab tab = new MMDCreativeTab( internalTabName, searchable, null );
		if( itemGroupsByModID.containsKey(modName) ) {
			itemGroupsByModID.get(modName).add(tab);
		} else {
			List<MMDCreativeTab> nl = new ArrayList<>();
			nl.add(tab);
			itemGroupsByModID.put(modName, nl);
		}
		
		return itemGroupsByModID.get(modName).size() - 1;
	}

	protected static MMDCreativeTab getTab( int id ) {
		return getTab( Loader.instance().activeModContainer().getModId(), id );
	}
	
	protected static MMDCreativeTab getTab( String modName, int id ) {
		if( itemGroupsByModID.containsKey(modName) ) {
			if( itemGroupsByModID.get(modName).size() > id ) {
				MMDCreativeTab t = itemGroupsByModID.get(modName).get(id);
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Gets a map of all items added, sorted by material
	 *
	 * @return An unmodifiable map of added items catagorized by metal material
	 */
	public static Map<String, List<MMDCreativeTab>> getItemsGroupsByModID() {
		return Collections.unmodifiableMap(itemGroupsByModID);
	}
	
}
