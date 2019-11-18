package mods.computercarts.network.message;

import io.netty.buffer.ByteBuf;
import mods.computercarts.common.items.ItemCartRemoteModule;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageConfigSync implements IMessage {

    public NBTTagCompound config = null;

    public MessageConfigSync() {
    }

    public MessageConfigSync(NBTTagCompound nbt) {
        config = (nbt == null) ? new NBTTagCompound() : nbt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        config = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, config);
    }

    public static class Handler implements IMessageHandler<MessageConfigSync, IMessage> {

        @Override
        public IMessage onMessage(MessageConfigSync msg, MessageContext ctx) {
            ItemCartRemoteModule.range = msg.config.getIntArray("remoterange");
            return null;
        }

    }
}