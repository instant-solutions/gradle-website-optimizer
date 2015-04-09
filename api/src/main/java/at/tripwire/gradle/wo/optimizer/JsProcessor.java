package at.tripwire.gradle.wo.optimizer;

import at.tripwire.gradle.wo.exceptions.ProcessException;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.Compiler;

import java.io.File;
import java.util.ArrayList;

public class JsProcessor implements Processor {

    @Override
    public String process(File[] srcFiles) throws ProcessException {
        CompilerOptions compilerOptions = new CompilerOptions();
        compilerOptions.setCodingConvention(new ClosureCodingConvention());
        compilerOptions.setOutputCharset("UTF-8");
        compilerOptions.setWarningLevel(DiagnosticGroups.CHECK_VARIABLES, CheckLevel.WARNING);
        compilerOptions.setLanguageIn(CompilerOptions.LanguageMode.ECMASCRIPT5);
        compilerOptions.setLanguageOut(CompilerOptions.LanguageMode.ECMASCRIPT5);
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(compilerOptions);

        Compiler compiler = new Compiler();
        compiler.disableThreads();
        compiler.initOptions(compilerOptions);

        ArrayList<SourceFile> inputFiles = new ArrayList<SourceFile>();

        for (File srcFile : srcFiles) {
            SourceFile inputFile = SourceFile.fromFile(srcFile);
            inputFiles.add(inputFile);
        }

        Result result = compiler.compile(new ArrayList<SourceFile>(), inputFiles, compilerOptions);

        if (!result.success) {
            throw new ProcessException("Failure when processing javascript files!");
        }

        return compiler.toSource();
    }
}
