package me.acablade.proximitychat.versions;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Proximity_1_16 implements IProximity{

    @Override
    public void processSending(PacketEvent event, double radius) {
        Player receiver = event.getPlayer();

        PacketContainer packetContainer = event.getPacket();
        UUID senderUUID = packetContainer.getUUIDs().read(0);
        Player sender = Bukkit.getPlayer(senderUUID);
        if(sender != null & receiver!=null)
            if(sender.getLocation().distance(receiver.getLocation()) > radius)
                event.setCancelled(true);
    }

    @Override
    public void processReceiving(PacketEvent event) {
        //ignored.
    }
}
