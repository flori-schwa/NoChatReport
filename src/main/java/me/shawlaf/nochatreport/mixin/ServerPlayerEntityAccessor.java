package me.shawlaf.nochatreport.mixin;

import net.minecraft.network.message.MessageType;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor {

    @Invoker("acceptsMessage")
    boolean invokeAcceptsMessage(RegistryKey<MessageType> typeKey);

    @Accessor("networkHandler")
    ServerPlayNetworkHandler getNetworkHandler();

}
