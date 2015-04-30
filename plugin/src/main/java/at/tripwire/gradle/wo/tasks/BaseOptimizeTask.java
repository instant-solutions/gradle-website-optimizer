package at.tripwire.gradle.wo.tasks;

import at.tripwire.gradle.wo.exceptions.ProcessException;
import at.tripwire.gradle.wo.optimizer.Processor;
import org.apache.commons.io.IOUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.invocation.Gradle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BaseOptimizeTask extends DefaultTask {

    private File destFile;
    private List<File> srcFiles = new ArrayList<>();

    protected void optimize(Processor processor) {
        destFile.getParentFile().mkdirs();

        try {
            String result = processor.process(srcFiles.toArray(new File[srcFiles.size()]));
            try (OutputStream fileOut = new FileOutputStream(destFile)) {
                IOUtils.write(result, fileOut);
            } catch (IOException e) {
                throw new GradleScriptException("Failed to write optimized file!", e);
            }
        } catch (ProcessException e) {
            throw new GradleScriptException("Failed to process files!", e);
        }
    }

    public void setSrcFiles(List<File> srcFiles) {
        this.srcFiles = srcFiles;
    }

    public void setDestFile(File destFile) {
        this.destFile = destFile;
    }

    public void src(File file) {
        srcFiles.add(file);
    }

    public void src(FileCollection files) {
        srcFiles.addAll(files.getFiles());
    }

    public void dest(File file) {
        this.destFile = file;
    }
}
