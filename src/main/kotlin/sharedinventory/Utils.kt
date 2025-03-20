package sharedinventory

import com.google.common.collect.ImmutableList
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.collection.DefaultedList
import java.util.concurrent.CompletableFuture

/// A [`PlayerInventory`], without the player.
class PlayerlessInventory {
    val main: DefaultedList<ItemStack>
    val armor: DefaultedList<ItemStack>
    val offHand: DefaultedList<ItemStack>
    val combinedInventory: List<DefaultedList<ItemStack>>


    constructor() {
        this.main = DefaultedList.ofSize(36, ItemStack.EMPTY)
        this.armor = DefaultedList.ofSize(4, ItemStack.EMPTY)
        this.offHand = DefaultedList.ofSize(1, ItemStack.EMPTY)
        this.combinedInventory = ImmutableList.of(
            this.main,
            this.armor,
            this.offHand
        )
    }
    constructor(inv: PlayerInventory) {
        this.main = inv.main
        this.armor = inv.armor
        this.offHand = inv.offHand
        this.combinedInventory = inv.combinedInventory
    }

    fun sync(inv: PlayerInventory) {
        inv.main = this.main
        inv.armor = this.armor
        inv.offHand = this.offHand
        inv.combinedInventory = this.combinedInventory
    }
}

object BooleanArgumentSuggester: SuggestionProvider<ServerCommandSource> {
    override fun getSuggestions(
        context: CommandContext<ServerCommandSource>?,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        builder.suggest("true")
        builder.suggest("false")

        return builder.buildFuture()
    }
}