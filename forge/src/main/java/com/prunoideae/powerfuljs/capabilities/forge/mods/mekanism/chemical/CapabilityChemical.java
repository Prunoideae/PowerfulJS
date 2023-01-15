package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;

import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

/**
 * 人活着哪有不疯的？硬撑罢了！
 * 人活着哪有不疯的？硬撑罢了！
 * 人活着哪有不疯的？硬撑罢了！
 * 人活着哪有不疯的？硬撑罢了！
 * 妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一
 * 拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈
 * 的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳
 * 把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！妈的，忍不了，一拳把地球打爆！他奶
 * 奶的鸡蛋六舅的哈密瓜妹妹的大窝瓜爷爷的大鸡腿婶婶的大葡萄妈妈的黄瓜菜爸爸的大面包三舅姥爷的大李子二婶的桃子三叔的西瓜七舅姥爷的小荔枝二舅
 * 姥爷的火龙果姑姑的猕猴桃祖爷爷的车厘子祖姥爷的大菠萝祖奶奶的大榴莲二爷的小草莓他三婶姥姥的大白菜他哥哥的大面条妹妹的小油菜弟弟的西葫芦姐
 * 姐的大土豆姐夫的大青椒爷爷的大茄子嗯啊，杀杀杀！！好可怕杀杀杀杀杀杀上勾拳！下勾拳！左勾拳！右勾拳！扫堂腿！回旋踢！这是蜘蛛吃耳屎，这是
 * 龙卷风摧毁停车场！这是羚羊蹬，这是山羊跳！乌鸦坐飞机！老鼠走迷宫！大象踢腿！愤怒的章鱼！巨斧砍大树！彻底疯狂！彻底疯狂！彻底疯狂！彻底疯
 * 狂！彻底疯狂！彻底疯狂！彻底疯狂！彻底疯狂！彻底疯狂！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀！杀
 * ！杀！彻底疯狂！彻底疯狂！彻底疯狂！彻底疯狂！彻底疯狂！彻底疯狂！彻底疯狂！彻底疯狂！彻底疯狂！彻底疯狂！
 */
public class CapabilityChemical {

    @FunctionalInterface
    public interface SetChemicalInTank<I, C extends Chemical<C>, S extends ChemicalStack<C>> {
        void accept(I instance, int slot, S stack);
    }

    @FunctionalInterface
    public interface InsertChemical<I, C extends Chemical<C>, S extends ChemicalStack<C>> {
        S apply(I instance, int slot, S stack, boolean simulate);
    }

    @FunctionalInterface
    public interface ExtractChemical<I, C extends Chemical<C>, S extends ChemicalStack<C>> {
        S apply(I instance, int slot, long amount, boolean simulate);
    }


    public static abstract class ChemicalBuilder<
            I,                                  //Instance
            H extends IChemicalHandler<C, S>,   //Handler
            C extends Chemical<C>,              //Chemical
            S extends ChemicalStack<C>          //Chemical Stack
            > extends CapabilityBuilderForge<I, H> {

        protected ToIntFunction<I> getTanks;
        protected BiFunction<I, Integer, S> getChemicalInTank;
        protected SetChemicalInTank<I, C, S> setChemicalInTank;
        protected BiFunction<I, Integer, Long> getTankCapacity;
        protected TriPredicate<I, Integer, S> isValid;
        protected InsertChemical<I, C, S> insertChemical;
        protected ExtractChemical<I, C, S> extractChemical;

        public ChemicalBuilder<I, H, C, S> getTanks(ToIntFunction<I> getTanks) {
            this.getTanks = getTanks;
            return this;
        }

        public ChemicalBuilder<I, H, C, S> getChemicalInTank(BiFunction<I, Integer, S> getChemicalInTank) {
            this.getChemicalInTank = getChemicalInTank;
            return this;
        }

        public ChemicalBuilder<I, H, C, S> setChemicalInTank(SetChemicalInTank<I, C, S> setChemicalInTank) {
            this.setChemicalInTank = setChemicalInTank;
            return this;
        }

        public ChemicalBuilder<I, H, C, S> getTankCapacity(BiFunction<I, Integer, Long> getTankCapacity) {
            this.getTankCapacity = getTankCapacity;
            return this;
        }

        public ChemicalBuilder<I, H, C, S> isValid(TriPredicate<I, Integer, S> isValid) {
            this.isValid = isValid;
            return this;
        }

        public ChemicalBuilder<I, H, C, S> insertChemical(InsertChemical<I, C, S> insertChemical) {
            this.insertChemical = insertChemical;
            return this;
        }

        public ChemicalBuilder<I, H, C, S> extractChemical(ExtractChemical<I, C, S> extractChemical) {
            this.extractChemical = extractChemical;
            return this;
        }
    }
}
