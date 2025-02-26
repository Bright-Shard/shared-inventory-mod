package sharedinventory

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.server.network.ServerPlayerEntity

object Config {
    var enabled = false
    var player: ServerPlayerEntity? = null
    var inv: PlayerInventory? = null
}