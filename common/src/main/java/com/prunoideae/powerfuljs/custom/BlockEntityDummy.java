package com.prunoideae.powerfuljs.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockEntityDummy extends BlockEntity {
    public static final Set<BlockDummyEntityJS.Builder> BLOCKS = new HashSet<>();
    public static final Map<ResourceLocation, BlockEntityType<BlockEntityDummy>> BLOCK_ENTITY_TYPES = new HashMap<>();


    public BlockEntityDummy(BlockPos blockPos, BlockState blockState) {
        super(BLOCK_ENTITY_TYPES.get(((BlockDummyEntityJS) (blockState.getBlock())).id), blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BLOCK_ENTITY_TYPES.get(((BlockDummyEntityJS) (getBlockState().getBlock())).id);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
}
