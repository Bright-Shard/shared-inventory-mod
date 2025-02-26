package sharedinventory.mixin;

import net.minecraft.screen.ScreenHandlerSyncHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import sharedinventory.IServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements IServerPlayerEntity {
    @Shadow
    @Final
    private ScreenHandlerSyncHandler screenHandlerSyncHandler;

    @Override
    public @NotNull ScreenHandlerSyncHandler getScreenHandlerSyncHandler() {
        return screenHandlerSyncHandler;
    }
}
