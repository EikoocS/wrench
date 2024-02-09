package tech.cookiepower.wrench;

import org.python.core.PyCode;

public record CompiledCode(String fileName,PyCode pyCode) {
}
