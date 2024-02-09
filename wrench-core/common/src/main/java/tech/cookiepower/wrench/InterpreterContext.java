package tech.cookiepower.wrench;

import lombok.Getter;
import org.python.core.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class InterpreterContext {
    final PySystemState state = new PySystemState();
    @Getter
    final InterpreterGlobals globals = new InterpreterGlobals();

    public void setIn(java.io.InputStream in){ state.stdin =new PyFile(in); }
    public void setIn(java.io.Reader in){ state.stdin =new PyFileReader(in); }
    public void setOut(java.io.OutputStream out){ state.stdout = new PyFile(out); }
    public void setOut(java.io.Writer out){ state.stdout = new PyFileWriter(out); }
    public void setErr(java.io.OutputStream err){ state.stderr = new PyFile(err); }
    public void setErr(java.io.Writer err){ state.stderr = new PyFileWriter(err); }

    @SuppressWarnings("unchecked")
    public List<String> getArgv(){
        return ((List<PyObject>) state.argv).stream()
                .map(arg -> {
                    if (!(arg instanceof PyString)) {
                        throw new IllegalStateException("argv contains non-string value");
                    }
                    return ((PyString) arg).getString();
                })
                .toList();
    }
    public void setArgv(List<String> argv){ state.argv = new PyList(argv.stream().map(PyString::new).toList()); }
    public void setArgv(String[] argv){ state.argv = new PyList(Stream.of(argv).map(PyString::new).toList()); }
    public void setArgv(Set<String> argv){ state.argv = new PyList(argv.stream().map(PyString::new).toList()); }

    public void addPath(String path){ state.path.append(new PyString(path)); }
    public void addModule(String name, InterpreterGlobals globals){
        var module = new PyModule(name, globals.pyGlobals);
        state.modules.__setitem__(name, module);
    }

    public void setGlobal(String key, Object value){ globals.put(key, value); }
    public Object getGlobal(String key){ return globals.get(key); }
    public <T> T getGlobalAs(String key,Class<T> type){ return globals.getAs(key, type); }
    public void setGlobals(InterpreterGlobals globals){ this.globals.clear(); this.globals.putAll(globals); }

    PyDictionary getPyGlobals(){ return globals.pyGlobals; }
}
