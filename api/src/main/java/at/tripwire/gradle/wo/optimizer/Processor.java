package at.tripwire.gradle.wo.optimizer;

import at.tripwire.gradle.wo.exceptions.ProcessException;

import java.io.File;

public interface Processor {
    String process(File[] srcFiles) throws ProcessException;
}
