package tech.cookiepower.wrench;

import lombok.Getter;
import org.python.core.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class InterpreterContext {
    final PySystemState state = new PySystemState();
    /**
     * Global variables that are available to all Python code executed in this context.
     * such as variables, functions, classes, etc.
     * */
    @Getter
    final InterpreterGlobals globals = new InterpreterGlobals();

    public void setIn(java.io.InputStream in){ state.stdin =new PyFile(in); }
    public void setIn(java.io.Reader in){ state.stdin =new PyFileReader(in); }
    public void setOut(java.io.OutputStream out){ state.stdout = new PyFile(out); }
    public void setOut(java.io.Writer out){ state.stdout = new PyFileWriter(out); }
    public void setErr(java.io.OutputStream err){ state.stderr = new PyFile(err); }
    public void setErr(java.io.Writer err){ state.stderr = new PyFileWriter(err); }

    /**
     * The list of command line arguments that are available to the Python code executed in this context.
     * */
    public void setArgv(List<String> argv){ state.argv = new PyList(argv.stream().map(PyString::new).toList()); }
    /**
     * The list of command line arguments that are available to the Python code executed in this context.
     * */
    public void setArgv(String[] argv){ state.argv = new PyList(Stream.of(argv).map(PyString::new).toList()); }
    /**
     * The list of command line arguments that are available to the Python code executed in this context.
     * */
    public void setArgv(Set<String> argv){ state.argv = new PyList(argv.stream().map(PyString::new).toList()); }
    /**
     * Add module search path.
     * can be import by Python code
     * @param path the path to add
     * */
    public void addPath(String path){ state.path.append(new PyString(path)); }
    /**
     * Add module.
     * can be import by Python code
     * @param name the name of the module
     * @param globals the global variables that are available to the module
     * */
    public void addModule(String name, InterpreterGlobals globals){
        var module = new PyModule(name, globals.pyGlobals);
        state.modules.__setitem__(name, module);
    }

    /**
     * set variable in global
     * @param key the name of the variable
     * @param value the value of the variable
     * */
    public void setGlobal(String key, Object value){ globals.put(key, value); }
    /**
     * get variable in global
     * @param key the name of the variable
     * */
    public Object getGlobal(String key){ return globals.get(key); }
    /**
     * get variable in global, and cast to type
     * @param key the name of the variable
     * @param type the type to cast the result to
     * */
    public <T> T getGlobalAs(String key,Class<T> type){ return globals.getAs(key, type); }
    /**
     * replace all global variables
     * @param globals new global variables
     * */
    public void setGlobals(InterpreterGlobals globals){ this.globals.clear(); this.globals.putAll(globals); }

    /**
     * tool method
     * */
    PyDictionary getPyGlobals(){ return globals.pyGlobals; }
}
