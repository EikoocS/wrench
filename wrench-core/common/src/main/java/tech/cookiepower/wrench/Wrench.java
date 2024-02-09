package tech.cookiepower.wrench;

public class Wrench {
    public static void main(String[] args) {
        var context0 = new InterpreterContext();
        var context1 = new InterpreterContext();
        var compiledCode1 = Interpreters.compile("""
                def test():
                    return "Hello, World!"
                ""","module0",Interpreters.CompileMode.EXEC);
        Interpreters.exec(compiledCode1,context0);
        context1.addModule("tm",context0.getGlobals());
        context1.addPath("S:/Wrench/temp");
        var compiledCode2 = Interpreters.compile("""
                import tm,ft
                print(tm.test())
                ft.test()
                ""","module1",Interpreters.CompileMode.EXEC);
        Interpreters.exec(compiledCode2,context1);
    }
}