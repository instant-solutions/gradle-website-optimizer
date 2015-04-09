package at.tripwire.gradle.wo.optimizer;

import at.tripwire.gradle.wo.exceptions.ProcessException;
import com.yahoo.platform.yui.compressor.CssCompressor;

import java.io.*;

public class CssProcessor implements Processor {

    public String process(File[] srcFiles) throws ProcessException {
        StringWriter writer = new StringWriter();

        try {
            for (File srcFile : srcFiles) {
                CssCompressor compressor = new CssCompressor(new InputStreamReader(new FileInputStream(srcFile)));
                compressor.compress(writer, -1);
            }
        } catch (IOException e) {
            throw new ProcessException("Failed to process CSS files.", e);
        }

        return writer.toString();
    }
}
