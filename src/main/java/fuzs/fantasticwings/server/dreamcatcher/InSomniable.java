package fuzs.fantasticwings.server.dreamcatcher;

import fuzs.fantasticwings.util.NBTSerializer;
import fuzs.fantasticwings.server.item.WingsItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Constants;

import java.util.function.IntConsumer;

public final class InSomniable {
    private State state;

    public InSomniable() {
        this(new SearchState());
    }

    private InSomniable(State state) {
        this.state = state;
    }

    public void onPlay(Level world, Player player, BlockPos pos, int note) {
        this.state = this.state.onPlay(world, player, pos, note);
    }

    public void clone(InSomniable other) {
        this.state = other.state.copy();
    }

    private interface State {
        State onPlay(Level world, Player player, BlockPos pos, int note);

        State copy();

        void ifSearching(IntConsumer consumer);
    }

    private static final class SearchState implements State {
        private final int[] mask = {
            0xBFBE,
            0xFFFD,
            0xFFFF,
            0xCD43,
            0xFFFF,
            0x7EFF,
            0xFFFF,
            0xF7FF,
            0xFBFF
        };

        private final String[] members = {
            "wings.dreamcatcher.jiu",
            "wings.dreamcatcher.sua",
            "wings.dreamcatcher.siyeon",
            "wings.dreamcatcher.handong",
            "wings.dreamcatcher.yoohyeon",
            "wings.dreamcatcher.dami",
            "wings.dreamcatcher.gahyeon",
        };

        private int state;

        private SearchState() {
            this(0x1FFFE);
        }

        private SearchState(int state) {
            this.state = state;
        }

        @Override
        public State onPlay(Level world, Player player, BlockPos pos, int note) {
            if (note >= 6 && note <= 14 && ((this.state = (this.state | this.mask[note - 6]) << 1) & 0x20000) == 0) {
                ItemStack stack = new ItemStack(WingsItems.ANGEL_WINGS_BOTTLE.get());
                stack.setHoverName(new TranslatableComponent(this.members[world.random.nextInt(this.members.length)]));
                ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.25D, pos.getZ() + 0.5D, stack);
                entity.setDefaultPickUpDelay();
                world.addFreshEntity(entity);
                return InSomniacState.INSTANCE;
            }
            return this;
        }

        @Override
        public State copy() {
            return new SearchState(this.state);
        }

        @Override
        public void ifSearching(IntConsumer consumer) {
            consumer.accept(this.state);
        }
    }

    private static final class InSomniacState implements State {
        private static final State INSTANCE = new InSomniacState();

        @Override
        public State onPlay(Level world, Player player, BlockPos pos, int note) {
            return this;
        }

        @Override
        public State copy() {
            return this;
        }

        @Override
        public void ifSearching(IntConsumer consumer) {
        }
    }

    public static final class Serializer implements NBTSerializer<InSomniable, CompoundTag> {
        private static final String SEARCH_STATE = "SearchState";

        @Override
        public CompoundTag serialize(InSomniable instance) {
            CompoundTag compound = new CompoundTag();
            instance.state.ifSearching(state -> compound.putInt(SEARCH_STATE, state));
            return compound;
        }

        @Override
        public InSomniable deserialize(CompoundTag compound) {
            State state;
            if (compound.contains(SEARCH_STATE, Constants.NBT.TAG_INT)) {
                state = new SearchState(compound.getInt(SEARCH_STATE));
            } else {
                state = InSomniacState.INSTANCE;
            }
            return new InSomniable(state);
        }
    }
}
