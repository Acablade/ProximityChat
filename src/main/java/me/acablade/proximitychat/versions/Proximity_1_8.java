package me.acablade.proximitychat.versions;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Proximity_1_8 implements IProximity{

    private Map<String, Player> stringPlayerMap;

    public Proximity_1_8(){
        this.stringPlayerMap = new HashMap<>();
    }

    @Override
    public void processSending(PacketEvent event, double radius) {

        Player receiver = event.getPlayer();
        PacketContainer packetContainer = event.getPacket();

        WrappedChatComponent wrappedChatComponent =packetContainer.getChatComponents().read(0);
        JsonObject jsonObject = new JsonParser().parse(wrappedChatComponent.getJson()).getAsJsonObject();
        String message = jsonObject.get("text").getAsString();

        Player sender = stringPlayerMap.get(message);

        if(sender!=null&&receiver!=null){
            if(sender.getLocation().distance(receiver.getLocation()) > radius){
                event.setCancelled(true);
                stringPlayerMap.remove(message);
            }
        }


    }

    @Override
    public void processReceiving(PacketEvent event) {

        Player sender = event.getPlayer();
        PacketContainer packetContainer = event.getPacket();

        String message = packetContainer.getStrings().read(0);
        stringPlayerMap.put(message,sender);

    }
}
