package io.github.apace100.apoli.command.argument;

import io.github.apace100.apoli.Apoli;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;

public class ApoliArgumentTypes {
    public static void register(){
        ArgumentTypeRegistry.registerArgumentType(
                Apoli.identifier("power"),
                PowerArgumentType.class,
                new PowerArgumentType.Serializer()
        );
        ArgumentTypeRegistry.registerArgumentType(
                Apoli.identifier("power_operation"),
                PowerOperationArgumentType.class,
                SingletonArgumentInfo.contextFree(PowerOperationArgumentType::new)
        );
        ArgumentTypeRegistry.registerArgumentType(
                Apoli.identifier("power_holder"),
                PowerHolderArgumentType.class,
                new EntityArgument.Info()
        );
    }
}
