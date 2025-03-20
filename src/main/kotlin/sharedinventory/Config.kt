package sharedinventory

import java.util.UUID

object Config {
    var enabled = false
    val sharedInventory = PlayerlessInventory()
    val unsharedPlayerInventories = HashMap<UUID, PlayerlessInventory>()
}