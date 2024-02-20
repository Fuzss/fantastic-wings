package fuzs.fantasticwings.client.flight.state;

import fuzs.fantasticwings.client.flight.Animator;
import fuzs.fantasticwings.server.flight.Flight;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;

public final class StateIdle extends State {
    public StateIdle() {
        super(Animator::beginIdle);
    }

    @Override
    protected State createIdle() {
        return this;
    }

    @Override
    protected State getDescent(Flight flight, Player player) {
        BlockPos below = new BlockPos(player.getX(), player.getY() - 0.25D, player.getZ());
        if (player.level.isEmptyBlock(below) && player.level.isEmptyBlock(below.below())) {
            return super.getDescent(flight, player);
        }
        return this.createIdle();
    }
}
