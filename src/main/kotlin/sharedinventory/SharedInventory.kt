package sharedinventory

import com.mojang.brigadier.Command
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.server.command.CommandManager
import net.minecraft.text.Text
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sharedinventory.utils.BooleanArgumentType
import sharedinventory.utils.BooleanEnum

object SharedInventory : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("sharedinventory")

	override fun onInitialize() {
		BooleanArgumentType.load()

		CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
			dispatcher.register(
				CommandManager.literal("shareInventory")
					.requires { source -> source.hasPermissionLevel(4) }
					.then(
						CommandManager.argument("enabled", BooleanArgumentType)
							.executes { command ->
								val enabled = command.getArgument("enabled", BooleanEnum::class.java).toBoolean()
								val server = command.source.server
								val label = "Shared inventory ${if (enabled) "enabled" else "disabled"}"

								LOGGER.info("shareInventory called: $label")
								LOGGER.info("Server: ${server.name}")
								server.playerManager.broadcast(Text.of(label), true)

								if (Config.player == null) {
									// TODO: Store separate data for each server
									// TODO: Persist inventory across reload
									LOGGER.info("Generating fake player and inventory...")
									Config.player = FakePlayer.get(server.worlds.first())
									Config.inv = PlayerInventory(Config.player)
								}

								if (enabled != Config.enabled) {
									LOGGER.info("Changing inventories")
									Config.enabled = enabled;

									if (enabled) {
										for (player in server.playerManager.playerList) {
											val iPlayer = player as IPlayerEntity
											val iServerPlayer = player as IServerPlayerEntity

											iPlayer.setInventory(Config.inv!!)

											iServerPlayer.getScreenHandlerSyncHandler().updateSlot(
												player.currentScreenHandler,
												0,
												ItemStack.EMPTY
											)
											iServerPlayer.getScreenHandlerSyncHandler().updateState(
												player.currentScreenHandler,
												Config.inv!!.main,
												ItemStack.EMPTY,
												intArrayOf()
											)
										}
									} else {
										for (player in server.playerManager.playerList) {
											val iPlayer = player as IPlayerEntity
											val iServerPlayer = player as IServerPlayerEntity

											iPlayer.resetInventory()

											iServerPlayer.getScreenHandlerSyncHandler().updateState(
												player.currentScreenHandler,
												Config.inv!!.main,
												ItemStack.EMPTY,
												intArrayOf()
											)
										}
									}
								}

								Command.SINGLE_SUCCESS
							}
					)
			)
		}
	}
}