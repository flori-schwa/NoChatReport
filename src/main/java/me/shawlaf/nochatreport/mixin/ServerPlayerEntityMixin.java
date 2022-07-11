package me.shawlaf.nochatreport.mixin;

import net.minecraft.class_7604;
import net.minecraft.client.option.ChatVisibility;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Decoration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    /**
     * @reason Send all chat messages as System messages to disable reporting functionality
     * @author shawlaf
     */
    @Overwrite
    public void sendChatMessage(class_7604 arg, MessageType.class_7602 arg2) {
        ServerPlayerEntity thisPlayer = (ServerPlayerEntity) ((Object) this);
        ServerPlayerEntityAccessor accessor = ((ServerPlayerEntityAccessor) thisPlayer);

        MessageType messageType = arg2.chatType();
        Decoration decoration = messageType.chat();

        if (thisPlayer.getClientChatVisibility() == ChatVisibility.FULL) {
            accessor.getNetworkHandler().sendPacket(
                    new GameMessageS2CPacket(
                            decoration.apply(arg.method_44852().getContent(), arg2),
                            false
                    )
            );
        }
    }
}
