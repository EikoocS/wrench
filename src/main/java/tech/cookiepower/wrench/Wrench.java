package tech.cookiepower.wrench;

import org.bukkit.plugin.java.JavaPlugin;
import tech.cookiepower.wrench.standard.EventNameMap;

import java.util.logging.Logger;

public class Wrench extends JavaPlugin {
    public static Logger logger;
    public static Wrench plugin;
    @Override
    public void onEnable() {
        logger = getLogger();
        plugin = this;
        EventNameMap.init();
        ScriptManager.getInstance();
    }
}
