package sharedinventory;

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerSyncHandler

// Interfaces that expose mixin classes' methods/fields

interface IPlayerInventory {

}

interface IPlayerEntity {
    // Set the player's inventory.
    fun setInventory(newInventory: PlayerInventory);
    // Reset the player's inventory to the value it was before calling `setInventory`.
    fun resetInventory();
}

interface IServerPlayerEntity {
    fun getScreenHandlerSyncHandler(): ScreenHandlerSyncHandler;
}