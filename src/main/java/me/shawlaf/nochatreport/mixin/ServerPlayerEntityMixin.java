package me.shawlaf.nochatreport.mixin;

import net.minecraft.network.message.MessageSender;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    /**
     * @reason Send all chat messages as System messages to disable reporting functionality
     * @author shawlaf
     */
    @Overwrite
    public void sendChatMessage(SignedMessage message, MessageSender sender, RegistryKey<MessageType> typeKey) {
        ServerPlayerEntity thisPlayer = (ServerPlayerEntity) ((Object) this);
        ServerPlayerEntityAccessor accessor = ((ServerPlayerEntityAccessor) thisPlayer);
        Registry<MessageType> messageTypeRegistry = thisPlayer.getWorld().getRegistryManager().get(Registry.MESSAGE_TYPE_KEY);

        MessageType messageType = messageTypeRegistry.get(typeKey);

        messageType.chat().ifPresent(displayRule -> {
            if (accessor.invokeAcceptsMessage(typeKey)) {
                accessor.getNetworkHandler().sendPacket(
                        new GameMessageS2CPacket(
                                displayRule.apply(message.getContent(), sender),
                                messageTypeRegistry.getRawId(messageTypeRegistry.get(MessageType.SYSTEM)) // Sending the actual message type will "format the message twice"
                        )
                );
            }
        });
    }
}
