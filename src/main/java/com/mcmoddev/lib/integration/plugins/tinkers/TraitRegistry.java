package com.mcmoddev.lib.integration.plugins.tinkers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mcmoddev.basemetals.BaseMetals;

import slimeknights.tconstruct.library.traits.ITrait;

public class TraitRegistry {

	private static Map<String, ITrait> registeredTraits = new HashMap<>();

	private TraitRegistry() {
		throw new IllegalAccessError("Not an instantiable class");
	}

	public static void addTrait(String name, ITrait trait) {
		registeredTraits.put(name, trait);
	}

	public static boolean hasTrait(String name) {
		return registeredTraits.keySet().contains(name);
	}

	public static ITrait getTrait(String name) {
		return registeredTraits.get(name);
	}

	public static ITrait get(String name) {
		return getTrait(name);
	}

	public static void initTiCTraits() {
		Field[] fields = slimeknights.tconstruct.tools.TinkerTraits.class.getDeclaredFields();
		registerFieldArray(fields);
	}

	public static void initMetalsTraits() {
		Field[] fields = com.mcmoddev.lib.integration.plugins.tinkers.traits.MMDTraits.class.getDeclaredFields();
		registerFieldArray(fields);
	}

	public static void initTAIGATraits() {
		Field[] fields = com.sosnitzka.taiga.MaterialTraits.class.getDeclaredFields();
		registerFieldArray(fields);
	}

	private static void registerFieldArray(Field[] regList) {
		for (Field f : regList) {
			Class<?> clazz = f.getType();
			try {
				registeredTraits.put(f.getName(), (ITrait) f.get(clazz));
			} catch (final Exception ex) {
				BaseMetals.logger.error("tried to register trait " + f.getName() + ", caught exception", ex);
			}
		}
	}

	public static void dumpRegistry() {
		for (Entry<String, ITrait> e : registeredTraits.entrySet()) {
			String t = String.format("BaseMetals-TCon> Trait: %s - class %s", e.getKey(), e.getValue().getClass().getName());
			BaseMetals.logger.info(t);
		}
	}

}
