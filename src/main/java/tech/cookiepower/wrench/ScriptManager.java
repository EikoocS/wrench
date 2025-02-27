package tech.cookiepower.wrench;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;
import tech.cookiepower.wrench.standard.StandardUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ScriptManager {
    private static ScriptManager instance = null;

    private ModuleScriptProvider scriptProvider;
    private RequireBuilder requireBuilder;
    private Scriptable standardScope;

    private ScriptManager(){
        var scriptFile = new File("scripts/");
        if(!scriptFile.exists()){
            scriptFile.mkdirs();
        }
        var scriptURI = scriptFile.toURI();
        var scriptURIList = List.of(scriptURI);
        var urlProvider = new UrlModuleSourceProvider(scriptURIList,null);
        scriptProvider = new SoftCachingModuleScriptProvider(urlProvider);

        requireBuilder = new RequireBuilder()
                .setModuleScriptProvider(scriptProvider)
                .setSandboxed(true);

        try(Context context = Context.enter()){
            standardScope = context.initStandardObjects();
            var scripts = scriptFile.listFiles((file, name) -> name.endsWith(".js"));
            for(var script : scripts){
                var scope = buildScope();
                var reader = new java.io.FileReader(script);
                context.evaluateReader(scope, reader, script.getName(), 1, null);
            }
        }catch (IOException e){
            Wrench.logger.warning("Failed to load script: " + e.getMessage());
        }
    }

    private Scriptable buildScope(){
        try(Context context = Context.enter()){
            Scriptable scope = context.newObject(standardScope);
            scope.setPrototype(standardScope);
            scope.setParentScope(null);
            requireBuilder.createRequire(context, scope).install(scope);
            ScriptableObject.putProperty(scope,"plugin",Wrench.plugin);
            StandardUtil.importStandard(scope);
            return scope;
        }
    }

    public static ScriptManager getInstance(){
        if(instance == null){
            instance = new ScriptManager();
        }
        return instance;
    }
}
