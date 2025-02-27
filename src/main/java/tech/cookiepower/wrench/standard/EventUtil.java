package tech.cookiepower.wrench.standard;

import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.NotNull;
import org.mozilla.javascript.ArrowFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import tech.cookiepower.wrench.Wrench;

import java.lang.reflect.Method;

public class EventUtil extends StandardUtil{
    public final EventPriority lowest = EventPriority.LOWEST;
    public final EventPriority low = EventPriority.LOW;
    public final EventPriority normal = EventPriority.NORMAL;
    public final EventPriority high = EventPriority.HIGH;
    public final EventPriority highest = EventPriority.HIGHEST;
    public final EventPriority monitor = EventPriority.MONITOR;
    public static final Listener empryListener = new Listener() {};

    EventUtil(Scriptable scope) {
        super(scope);
    }

    public RegisteredListener listener(String eventName, ArrowFunction executable){
        var event = EventNameMap.match(eventName);
        return listener(event,executable);
    }

    public RegisteredListener listener(Class<? extends Event> target, ArrowFunction executable){
        return listener(target,executable,normal,false);
    }

    public RegisteredListener listener(Class<? extends Event> target, ArrowFunction executable,EventPriority priority,boolean ignoreCancelled){
        var registeredListener = new RegisteredListener(empryListener, new ScriptEventExecutor(scope,executable), priority, Wrench.plugin, ignoreCancelled);
        var handlerList = getEventListeners(target);
        handlerList.register(registeredListener);
        return registeredListener;
    }

    class ScriptEventExecutor implements EventExecutor{
        private final ArrowFunction executable;
        public ScriptEventExecutor(Scriptable scope ,ArrowFunction executable){
            this.executable = executable;
        }

        @Override
        public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
            try(var context = Context.enter()){
                executable.call(context,scope,executable,new Object[]{event});
            }catch (Exception e){
                Wrench.logger.warning(e.getLocalizedMessage());
            }
        }
    }

    /// copy form io.papermc.paper.plugin.manager.PaperEventManager.getEventListeners()
    private @NotNull HandlerList getEventListeners(@NotNull Class<? extends Event> type) {
        try {
            Method method = this.getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList)method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }
    /// copy from io.papermc.paper.plugin.manager.PaperEventManager.getRegistrationClass()
    private @NotNull Class<? extends Event> getRegistrationClass(@NotNull Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException var3) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return this.getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }
}
