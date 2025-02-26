package sharedinventory.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import sharedinventory.IPlayerEntity;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements IPlayerEntity {
    @Shadow
    @Final
    @Mutable
    PlayerInventory inventory;

    @Unique
    PlayerInventory normalInventory;

    @Override
    public void setInventory(@NotNull PlayerInventory newInventory) {
        this.normalInventory = this.inventory;
        this.inventory = newInventory;
    }
    @Override
    public void resetInventory() {
        this.inventory = this.normalInventory;
    }
}
