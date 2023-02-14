package com.prunoideae.powerfuljs.custom;

import com.prunoideae.powerfuljs.CapabilityBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.custom.BasicBlockJS;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockDummyEntityJS extends BasicBlockJS implements EntityBlock {
    protected final ResourceLocation id;

    public static class Builder extends BasicBlockJS.Builder {
        public final List<CapabilityBuilder<BlockEntity, ?, ?>> capabilityBuilders = new ArrayList<>();

        public Builder(ResourceLocation i) {
            super(i);
            BlockEntityDummy.BLOCKS.add(this);
        }

        @Override
        public Block createObject() {
            return new BlockDummyEntityJS(this);
        }

        public Builder attachCapability(CapabilityBuilder<BlockEntity, ?, ?> capabilityBuilder) {
            this.capabilityBuilders.add(capabilityBuilder);
            return this;
        }
    }

    public BlockDummyEntityJS(BlockBuilder p) {
        super(p);
        id = p.id;
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new BlockEntityDummy(blockPos, blockState);
    }

}
