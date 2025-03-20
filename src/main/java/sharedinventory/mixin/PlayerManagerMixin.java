package sharedinventory.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sharedinventory.Config;
import sharedinventory.PlayerlessInventory;

import java.util.Map;
import java.util.UUID;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Shadow @Final private Map<UUID, ServerPlayerEntity> playerMap;
    @Shadow @Final private MinecraftServer server;

    @Inject(method = "respawnPlayer", at = @At("TAIL"))
    public void respawnPlayer(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ServerPlayerEntity actualPlayer = this.playerMap.get(player.getUuid());
        if (Config.INSTANCE.getEnabled()) {
            Config.INSTANCE.getSharedInventory().sync(actualPlayer.getInventory());
        }
    }

    /**
     * @author shush
     * @reason silence warning
     */
    @Overwrite
    public ServerPlayerEntity createPlayer(GameProfile profile, SyncedClientOptions syncedOptions) {
        ServerPlayerEntity entity = new ServerPlayerEntity(this.server, this.server.getOverworld(), profile, syncedOptions);

        if (Config.INSTANCE.getEnabled()) {
            Config.INSTANCE.getSharedInventory().sync(entity.getInventory());
        }

        return entity;
    }
}
