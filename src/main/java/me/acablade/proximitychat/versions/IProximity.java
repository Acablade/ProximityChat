package me.acablade.proximitychat.versions;

import com.comphenix.protocol.events.PacketEvent;

public interface IProximity {

    void processSending(PacketEvent event, double radius);

    void processReceiving(PacketEvent event);

}
