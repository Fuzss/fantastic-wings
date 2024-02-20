package fuzs.fantasticwings.server.flight;

import fuzs.fantasticwings.server.apparatus.FlightApparatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public interface Flight {
    default void setIsFlying(boolean isFlying) {
        this.setIsFlying(isFlying, PlayerSet.empty());
    }

    void setIsFlying(boolean isFlying, PlayerSet players);

    boolean isFlying();

    default void toggleIsFlying(PlayerSet players) {
        this.setIsFlying(!this.isFlying(), players);
    }

    void setTimeFlying(int timeFlying);

    int getTimeFlying();

    default void setWing(FlightApparatus wing) {
        this.setWing(wing, PlayerSet.empty());
    }

    void setWing(FlightApparatus wing, PlayerSet players);

    FlightApparatus getWing();

    float getFlyingAmount(float delta);

    void registerFlyingListener(FlyingListener listener);

    void registerSyncListener(SyncListener listener);

    boolean canFly(Player player);

    boolean canLand(Player player);

    void tick(Player player);

    void onFlown(Player player, Vec3 direction);

    void clone(Flight other);

    void sync(PlayerSet players);

    void serialize(FriendlyByteBuf buf);

    void deserialize(FriendlyByteBuf buf);

    interface FlyingListener {
        void onChange(boolean isFlying);

        static Consumer<FlyingListener> onChangeUsing(boolean isFlying) {
            return l -> l.onChange(isFlying);
        }
    }

    interface SyncListener {
        void onSync(PlayerSet players);

        static Consumer<SyncListener> onSyncUsing(PlayerSet players) {
            return l -> l.onSync(players);
        }
    }

    interface PlayerSet {
        void notify(Notifier notifier);

        static PlayerSet empty() {
            return n -> {
            };
        }

        static PlayerSet ofSelf() {
            return Notifier::notifySelf;
        }

        static PlayerSet ofPlayer(ServerPlayer player) {
            return n -> n.notifyPlayer(player);
        }

        static PlayerSet ofOthers() {
            return Notifier::notifyOthers;
        }

        static PlayerSet ofAll() {
            return n -> {
                n.notifySelf();
                n.notifyOthers();
            };
        }
    }

    interface Notifier {
        void notifySelf();

        void notifyPlayer(ServerPlayer player);

        void notifyOthers();

        static Notifier of(Runnable notifySelf, Consumer<ServerPlayer> notifyPlayer, Runnable notifyOthers) {
            return new Notifier() {
                @Override
                public void notifySelf() {
                    notifySelf.run();
                }

                @Override
                public void notifyPlayer(ServerPlayer player) {
                    notifyPlayer.accept(player);
                }

                @Override
                public void notifyOthers() {
                    notifyOthers.run();
                }
            };
        }
    }
}
