package tech.cookiepower.wrench;

import lombok.experimental.UtilityClass;
import org.python.core.CompilerFlags;
import org.python.core.Py;
import org.python.util.PythonInterpreter;

import java.io.InputStream;

/**
 * The core of the Wrench library, containing the main methods for interacting with the Jython runtime.
 * @see InterpreterGlobals
 * @see InterpreterContext
 * @see CompiledCode
 * */
@UtilityClass
@SuppressWarnings("unused")
public class Interpreters {
    // Enable UTF-8 source code parsing
    private final CompilerFlags COMPILER_FLAGS = new CompilerFlags(CompilerFlags.PyCF_SOURCE_IS_UTF8);

    /**
     * Initializes the Jython runtime. This should only be called once, before any other Python objects (including PythonInterpreter) are created.
     * */
    public void initialize(){
        PythonInterpreter.initialize(System.getProperties(), null, new String[] {});
    }

    /**
     * Compiles the given Python code from inputStream into a CompiledCode object.
     * @param code the code to compile as an input stream
     * @param fileName the name of the file the code is from,will be show in stack trace
     * @param mode the mode to compile the code in (EXEC, EVAL, SINGLE)
     *             EXEC: on parse module
     *             EVAL: on parse expression
     *             SINGLE: on parse interactive
     * */
    public CompiledCode compile(InputStream code, String fileName, CompileMode mode){
        var pyCode = Py.compile_flags(code, fileName, mode.pyMode, COMPILER_FLAGS);
        return new CompiledCode(pyCode);
    }

    /**
     * Compiles the given Python code from String into a CompiledCode object.
     * @param code the code to compile
     * @param fileName the name of the file the code is from,will be show in stack trace
     * @param mode the mode to compile the code in (EXEC, EVAL, SINGLE)
     *             EXEC: on parse module
     *             EVAL: on parse expression
     *             SINGLE: on parse interactive
     * */
    public CompiledCode compile(String code,String fileName,CompileMode mode){
        var pyCode = Py.compile_flags(code, fileName, mode.pyMode, COMPILER_FLAGS);
        return new CompiledCode(pyCode);
    }

    /**
     * Executes the given Python code and returns the result.
     * @param code the code to compile
     * @param context the context to compile the code in
     * @return the result of execution
     * */
    public Object eval(CompiledCode code,InterpreterContext context){
        Py.setSystemState(context.state);
        return Py.runCode(code.pyCode(),null,context.getPyGlobals());
    }

    /**
     * Executes the given Python code and returns the result, cast to the given type.
     * @param code compiled code to execute
     * @param context the context to execute the code in
     * @param type the type to cast the result to
     * @return the result of execution, cast to the given type
     * */
    public <T> T evalAs(CompiledCode code,InterpreterContext context,Class<T> type){
        return type.cast(Py.runCode(code.pyCode(),null,context.getPyGlobals()));
    }

    /**
     * Executes the given Python code.
     * @param code compiled code to execute
     * @param context the context to execute the code in
     * */
    public void exec(CompiledCode code,InterpreterContext context){
        Py.setSystemState(context.state);
        Py.runCode(code.pyCode(),null,context.getPyGlobals());
        Py.flushLine();
    }


    public enum CompileMode{
        /** on parse module */
        EXEC(org.python.core.CompileMode.exec),
        /** on parse expression */
        EVAL(org.python.core.CompileMode.eval),
        /** on parse interactive*/
        SINGLE(org.python.core.CompileMode.single);

        final org.python.core.CompileMode pyMode;
        CompileMode(org.python.core.CompileMode pyMode){
            this.pyMode = pyMode;
        }
    }
}
