package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;

public class MekanismHelper {
    //TODO: add type wrappers, but I don't wanna
    public static GasStack asGasStack(Gas gas, long amount) {
        return new GasStack(gas, amount);
    }

    public static InfusionStack asInfusionStack(InfuseType infuseType, long amount) {
        return new InfusionStack(infuseType, amount);
    }

    public static PigmentStack asPigmentStack(Pigment pigment, long amount) {
        return new PigmentStack(pigment, amount);
    }

    public static SlurryStack asSlurryStack(Slurry slurry, long amount) {
        return new SlurryStack(slurry, amount);
    }
}
