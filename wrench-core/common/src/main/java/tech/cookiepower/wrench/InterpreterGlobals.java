package tech.cookiepower.wrench;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.core.PyString;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class InterpreterGlobals implements Map<String,Object> {
    final PyDictionary pyGlobals;

    /**
     * Create a new InterpreterGlobals object with an empty set of global variables.
     * */
    public InterpreterGlobals(){
        pyGlobals = new PyDictionary();
    }
    /**
     * Create a new InterpreterGlobals object with the given set of global variables.
     * */
    public InterpreterGlobals(PyDictionary globals){
        this.pyGlobals = globals;
    }
    /**
     * Create a new InterpreterGlobals object with the given set of global variables.
     * */
    public InterpreterGlobals(Map<String,Object> globals){
        var castGlobals = new HashMap<PyObject, PyObject>();
        for (var entry : globals.entrySet()) {
            castGlobals.put(new PyString(entry.getKey()), Py.java2py(entry.getValue()));
        }
        this.pyGlobals = new PyDictionary(castGlobals);
    }

    // implement Map<String,Object>
    @Override public int size() { return pyGlobals.size(); }
    @Override public boolean isEmpty() { return pyGlobals.isEmpty(); }
    @Override public boolean containsKey(Object key) { return pyGlobals.containsKey(key); }
    @Override public boolean containsValue(Object value) { return pyGlobals.containsValue(value); }
    @Override public Object get(Object key) { return pyGlobals.get(key.toString()); }
    /**
     * get the value of the variable with the given name as the given type.
     * @param key the name of the variable
     * @param type the type to cast the variable to
     * */
    @Nullable
    public <T> T getAs(String key, Class<T> type){ return Py.tojava((PyObject) pyGlobals.get(key), type); }
    @Override public Object put(String key, Object value) { return pyGlobals.put(new PyString(key),value); }
    @Override public Object remove(Object key) { return pyGlobals.remove(key); }
    @Override
    public void putAll(@NotNull Map<? extends String, ?> m) {
        for (var entry : m.entrySet()) {
            pyGlobals.put(new PyString(entry.getKey()), Py.java2py(entry.getValue()));
        }
    }
    @Override public void clear() { pyGlobals.clear(); }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Set<String> keySet() {
        return (Set<String>) pyGlobals.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.toUnmodifiableSet());
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Object> values() {
        return (Collection<Object>) pyGlobals.values().stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Set<Entry<String, Object>> entrySet() {
        return ((Set<Entry<Object, PyObject>>) pyGlobals.entrySet()).stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(
                        entry.getKey().toString(),
                        Py.tojava(entry.getValue(), Object.class)
                        )
                )
                .collect(Collectors.toUnmodifiableSet());
    }
}
