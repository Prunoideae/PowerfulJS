package com.prunoideae.powerfuljs.capabilities.forge.mods.botania.mana;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;

@RemapPrefixForJS("pjs$")
public class ManaHandlerJS implements ManaReceiver, INBTSerializable<IntTag> {

    @FunctionalInterface
    public interface GetCurrentMana {
        int get(BlockEntity instance, ManaHandlerJS storage);
    }

    @FunctionalInterface
    public interface IsManaFull {
        boolean test(BlockEntity instance, ManaHandlerJS storage);
    }

    @FunctionalInterface
    public interface ReceiveMana {
        void receiveMana(BlockEntity instance, ManaHandlerJS storage, int mana);
    }

    private final BlockEntity instance;
    public int mana;
    private final int capacity;
    private final GetCurrentMana getCurrentMana;
    private final IsManaFull isManaFull;
    private final ReceiveMana receiveMana;

    public ManaHandlerJS(BlockEntity instance, int capacity, GetCurrentMana getCurrentMana, IsManaFull isManaFull, ReceiveMana receiveMana) {
        this.instance = instance;
        this.capacity = capacity;
        this.getCurrentMana = getCurrentMana;
        this.isManaFull = isManaFull;
        this.receiveMana = receiveMana;
    }

    @Override
    public IntTag serializeNBT() {
        return IntTag.valueOf(mana);
    }

    @Override
    public void deserializeNBT(IntTag arg) {
        mana = arg.getAsInt();
    }

    @Override
    public Level getManaReceiverLevel() {
        return instance.getLevel();
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return instance.getBlockPos();
    }

    @Override
    public int getCurrentMana() {
        if (getCurrentMana != null) return getCurrentMana.get(instance, this);
        return mana;
    }

    @Override
    public boolean isFull() {
        if (isManaFull != null) return isManaFull.test(instance, this);
        return getCurrentMana() < capacity;
    }

    @Override
    @HideFromJS
    public void receiveMana(int mana) {
        if (receiveMana != null) receiveMana.receiveMana(instance, this, mana);
        else this.mana += mana;
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        // Since it's impossible for us to get mana collector/spreader implemented.
        return true;
    }

    public static class Builder extends CapabilityBuilderForge<BlockEntity, ManaReceiver> {

        protected final int capacity;
        protected GetCurrentMana getCurrentMana;
        protected IsManaFull isManaFull;
        protected ReceiveMana receiveMana;

        public Builder getCurrentMana(GetCurrentMana getCurrentMana) {
            this.getCurrentMana = getCurrentMana;
            return this;
        }

        public Builder isManaFull(IsManaFull isManaFull) {
            this.isManaFull = isManaFull;
            return this;
        }

        public Builder receiveMana(ReceiveMana receiveMana) {
            this.receiveMana = receiveMana;
            return this;
        }

        public Builder(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public ManaReceiver getCapability(BlockEntity instance) {
            return new ManaHandlerJS(instance, capacity, getCurrentMana, isManaFull, receiveMana);
        }

        @Override
        public Capability<ManaReceiver> getCapabilityKey() {
            return BotaniaForgeCapabilities.MANA_RECEIVER;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerfuljs:mana");
        }
    }
}
