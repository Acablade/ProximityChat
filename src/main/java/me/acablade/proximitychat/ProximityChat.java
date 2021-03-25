package me.acablade.proximitychat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.acablade.proximitychat.versions.IProximity;
import me.acablade.proximitychat.versions.Proximity_1_16;
import me.acablade.proximitychat.versions.Proximity_1_8;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;


public final class ProximityChat extends JavaPlugin implements CommandExecutor {

    double radius;
    IProximity proximity;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();

        Logger.getLogger("Minecraft").info("The version of server is: "+getNMSVersion());
        if(getNMSVersion().contains("1_16")) proximity = new Proximity_1_16();
        else proximity = new Proximity_1_8();

        getCommand("proximitychat").setExecutor((CommandExecutor)this);
        radius = getConfig().getDouble("radius",100);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.HIGH, PacketType.Play.Server.CHAT, PacketType.Play.Client.CHAT){
            @Override
            public void onPacketSending(PacketEvent event) {
                proximity.processSending(event,radius);
            }

            @Override
            public void onPacketReceiving(PacketEvent event) {
                proximity.processReceiving(event);
            }
        });

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0){
            sender.sendMessage("§c/pc reload");
        }else if(args.length == 1){
            if(!sender.hasPermission("pc.reload")){
                sender.sendMessage("§cInsufficient permissions.");
                return false;
            }
            reloadConfig();
            radius = getConfig().getDouble("radius",100);
            sender.sendMessage("§eProximityChat has been reloaded!");
        }

        return false;
    }

    @Override
    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
        HandlerList.unregisterAll(this);
    }

    public static String getNMSVersion(){
        String v = Bukkit.getServer().getClass().getPackage().getName();
        return v.substring(v.lastIndexOf('.') + 1);
    }

}
