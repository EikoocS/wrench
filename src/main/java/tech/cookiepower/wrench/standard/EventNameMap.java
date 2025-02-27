package tech.cookiepower.wrench.standard;

import org.bukkit.event.Event;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventNameMap {
    private static List<String> eventPackage = List.of(
            "com.destroystokyo.paper.event.block",
            "com.destroystokyo.paper.event.brigadier",
            "com.destroystokyo.paper.event.entity",
            "com.destroystokyo.paper.event.executor",
            "com.destroystokyo.paper.event.executor.asm",
            "com.destroystokyo.paper.event.inventory",
            "com.destroystokyo.paper.event.player",
            "com.destroystokyo.paper.event.profile",
            "com.destroystokyo.paper.event.server",
            "io.papermc.paper.event.block",
            "io.papermc.paper.event.entity",
            "io.papermc.paper.event.packet",
            "io.papermc.paper.event.player",
            "io.papermc.paper.event.server",
            "io.papermc.paper.event.world",
            "io.papermc.paper.event.world.border",
            "io.papermc.paper.plugin.lifecycle.event.handler",
            "io.papermc.paper.plugin.lifecycle.event.handler.configuration",
            "io.papermc.paper.plugin.lifecycle.event.registrar",
            "io.papermc.paper.plugin.lifecycle.event.types",
            "io.papermc.paper.registry.event",
            "org.bukkit.event.block",
            "org.bukkit.event.command",
            "org.bukkit.event.enchantment",
            "org.bukkit.event.entity",
            "org.bukkit.event.hanging",
            "org.bukkit.event.inventory",
            "org.bukkit.event.player",
            "org.bukkit.event.raid",
            "org.bukkit.event.server",
            "org.bukkit.event.vehicle",
            "org.bukkit.event.weather",
            "org.bukkit.event.world",
            "org.spigotmc.event.player"
    );
    private static Map<String,Class<? extends Event>> eventMap = new HashMap<>();
    public static Class<? extends Event> match(String eventName){
        var clazz = eventMap.get(eventName);
        if(clazz==null){
            throw new RuntimeException("Event not found: " + eventName);
        }
        return clazz;
    }

    public static void init(){
        for(var packageName : eventPackage){
            Reflections reflections = new Reflections(packageName);
            var eventList = reflections.getSubTypesOf(Event.class);
            for(var event : eventList){
                var name = event.getSimpleName();
                if(name.endsWith("Event")){
                    name = name.substring(0,name.length()-5);
                }
                name = name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
                eventMap.put(name,event);
            }
        }
    }
}
