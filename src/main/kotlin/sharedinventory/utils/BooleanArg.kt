package sharedinventory.utils

import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry
import net.minecraft.command.argument.EnumArgumentType
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable

enum class BooleanEnum: StringIdentifiable {
    TRUE,
    FALSE;

    companion object {
        val CODEC: Codec<BooleanEnum> = StringIdentifiable.createBasicCodec(BooleanEnum::values)
    }

    override fun asString(): String {
        return when (this) {
            TRUE -> "true"
            FALSE -> "false"
        }
    }

    fun toBoolean(): Boolean {
        return when(this) {
            TRUE -> true
            FALSE -> false
        }
    }
}

object BooleanArgumentType: EnumArgumentType<BooleanEnum>(BooleanEnum.CODEC, BooleanEnum::values) {
    init {
        ArgumentTypeRegistry.registerArgumentType(
            Identifier.of("brightcraft", "boolean"),
            BooleanArgumentType::class.java,
            ConstantArgumentSerializer.of { -> BooleanArgumentType }
        )
    }

    fun load() {}
}