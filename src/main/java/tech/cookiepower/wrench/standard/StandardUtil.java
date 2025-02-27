package tech.cookiepower.wrench.standard;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public abstract class StandardUtil {
    protected Scriptable scope;
    StandardUtil(Scriptable scope){
        this.scope = scope;
    }

    public static void importStandard(Scriptable scope){
        put(scope,"event",new EventUtil(scope));
    }

    private static void put(Scriptable scope,String name,Object value){
        var wrapped =  Context.javaToJS(value, scope);
        ScriptableObject.putProperty(scope, name, wrapped);
    }
}
