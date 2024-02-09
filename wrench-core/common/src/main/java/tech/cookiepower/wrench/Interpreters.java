package tech.cookiepower.wrench;

import lombok.experimental.UtilityClass;
import org.python.core.CompilerFlags;
import org.python.core.Py;
import org.python.util.PythonInterpreter;

import java.io.InputStream;

@UtilityClass
public class Interpreters {
    // Enable UTF-8 source code parsing
    private final CompilerFlags COMPILER_FLAGS = new CompilerFlags(CompilerFlags.PyCF_SOURCE_IS_UTF8);

    /**
     * Initializes the Jython runtime. This should only be called once, before any other Python objects (including PythonInterpreter) are created.
     * */
    public void initialize(){
        PythonInterpreter.initialize(System.getProperties(), null, new String[] {});
    }


    public CompiledCode compile(InputStream code, String fileName, CompileMode mode){
        var pyCode = Py.compile_flags(code, fileName, mode.pyMode, COMPILER_FLAGS);
        return new CompiledCode(fileName, pyCode);
    }

    public CompiledCode compile(String code,String fileName,CompileMode mode){
        var pyCode = Py.compile_flags(code, fileName, mode.pyMode, COMPILER_FLAGS);
        return new CompiledCode(fileName, pyCode);
    }

    public Object eval(CompiledCode code,InterpreterContext context){
        Py.setSystemState(context.state);
        return Py.runCode(code.pyCode(),null,context.getPyGlobals());
    }

    public <T> T evalAs(CompiledCode code,InterpreterContext context,Class<T> type){
        return type.cast(Py.runCode(code.pyCode(),null,context.getPyGlobals()));
    }

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
