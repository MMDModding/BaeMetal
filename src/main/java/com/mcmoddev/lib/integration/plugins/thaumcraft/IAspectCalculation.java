package com.mcmoddev.lib.integration.plugins.thaumcraft;

import thaumcraft.api.aspects.Aspect;

/**
 * Allowance for a Lambda/anon-method for calculating the amount of an aspect
 * per item and/or block.
 * 
 * @author Daniel "DShadowWolf" Hazelton &lt;dshadowwolf@gmail.com&gt;
 * @since 26-OCT-2018
 */
public interface IAspectCalculation {
	public float apply(float mult);
//	public boolean canApply(TCMaterial material, Aspect aspect);
}
