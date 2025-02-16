package tech.cookiepower.wrench;

import org.bukkit.plugin.java.JavaPlugin;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class Wrench extends JavaPlugin {
    @Override
    public void onEnable() {
        try (Context context = Context.enter()){
            Scriptable scope = context.initStandardObjects();
            Object result = context.evaluateString(scope, "'Hello Rhino!'","<stdin>",1,null);
            getLogger().info(Context.toString(result));
        }catch (Exception e){
            getLogger().warning(e.getLocalizedMessage());
        }
    }
}
