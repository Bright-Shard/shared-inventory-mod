package sharedinventory.mixin;

import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import sharedinventory.IPlayerInventory;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements IPlayerInventory {
}
