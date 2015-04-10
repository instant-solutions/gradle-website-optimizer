package at.tripwire.gradle.wo.optimizer;

import at.tripwire.gradle.wo.exceptions.ProcessException;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

public class HtmlProcessor {

    public String process(File srcFile) throws ProcessException {
        HtmlCompressor compressor = new HtmlCompressor();
        compressor.setEnabled(true);
        compressor.setCompressCss(false);
        compressor.setCompressJavaScript(false);
        compressor.setGenerateStatistics(false);
        compressor.setRemoveComments(true);

        try {
            return compressor.compress(IOUtils.toString(srcFile.toURI()));
        } catch (IOException e) {
            throw new ProcessException("Failed to process HTML file!", e);
        }
    }
}
