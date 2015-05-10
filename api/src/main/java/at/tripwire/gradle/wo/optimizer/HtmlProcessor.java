package at.tripwire.gradle.wo.optimizer;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class HtmlProcessor {

    public String process(String src) {
        HtmlCompressor compressor = new HtmlCompressor();
        compressor.setEnabled(true);
        compressor.setCompressCss(false);
        compressor.setCompressJavaScript(false);
        compressor.setGenerateStatistics(false);
        compressor.setRemoveComments(true);

        return compressor.compress(src);
    }
}
