package sharedinventory

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.command.CommandManager
import net.minecraft.text.Text
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object SharedInventory: ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("sharedinventory")

	override fun onInitialize() {
		ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
			if (Config.enabled) {
				val player = handler.player
				Config.unsharedPlayerInventories[player.uuid] = PlayerlessInventory(player.inventory)
				Config.sharedInventory.sync(player.inventory)
			}
		}

		CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
			dispatcher.register(
				CommandManager.literal("shareInventory")
					.requires { source -> source.hasPermissionLevel(4) }
					.then(
						CommandManager.argument("enabled", StringArgumentType.word())
							.suggests(BooleanArgumentSuggester)
							.executes { command ->
								val server = command.source.server
								val enabledString = command.getArgument("enabled", String::class.java)
								val enabled = when (enabledString) {
									"true" -> true
									"false" -> false
									else -> {
										command.source.sendFeedback(
											{ Text.of("Requires 'true' or 'false'.") },
											false
										)
                                        return@executes 1
									};
								}

								LOGGER.info("shareInventory called, enabled: $enabled")

								if (enabled != Config.enabled) {
									LOGGER.info("Changing inventories")
									Config.enabled = enabled;

									if (enabled) {
										for (player in server.playerManager.playerList) {
											Config.unsharedPlayerInventories[player.uuid] = PlayerlessInventory(player.inventory)
											Config.sharedInventory.sync(player.inventory)
										}
									} else {
										for (player in server.playerManager.playerList) {
											Config.unsharedPlayerInventories[player.uuid]?.sync(player.inventory)
										}
									}

									val label = "Shared inventory ${if (enabled) "enabled" else "disabled"}"
									server.playerManager.broadcast(Text.of(label), true)
								}

								Command.SINGLE_SUCCESS
							}
					)
			)
		}
	}
}