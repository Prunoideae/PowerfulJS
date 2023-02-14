package com.prunoideae.powerfuljs.capabilities.forge.mods.immersive;

import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import blusunrize.immersiveengineering.api.shader.CapabilityShader;
import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import blusunrize.immersiveengineering.api.wires.GlobalWireNetwork;
import blusunrize.immersiveengineering.api.wires.NetHandlerCapability;
import blusunrize.immersiveengineering.api.wires.redstone.CapabilityRedstoneNetwork;
import net.minecraftforge.common.capabilities.Capability;

public interface CapabilitiesHelperIE {
    Capability<IRotationAcceptor> ROTATION_ACCEPTOR = IRotationAcceptor.CAPABILITY;
    Capability<ExternalHeaterHandler.IExternalHeatable> EXTERNAL_HEATABLE = ExternalHeaterHandler.CAPABILITY;
    Capability<GlobalWireNetwork> GLOBAL_WIRE_NETWORK = NetHandlerCapability.NET_CAPABILITY;
    Capability<CapabilityRedstoneNetwork.RedstoneBundleConnection> REDSTONE_BUNDLE_CONNECTION = CapabilityRedstoneNetwork.REDSTONE_BUNDLE_CONNECTION;
    Capability<CapabilityShader.ShaderWrapper> SHADER_WRAPPER = CapabilityShader.SHADER_CAPABILITY;
}
