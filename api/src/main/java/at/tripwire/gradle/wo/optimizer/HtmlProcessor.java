package at.tripwire.gradle.wo.optimizer;

import at.tripwire.gradle.wo.options.HtmlOptions;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class HtmlProcessor {

    private HtmlOptions options = new HtmlOptions();

    public String process(String src) {
        HtmlCompressor compressor = new HtmlCompressor();
        compressor.setEnabled(true);
        compressor.setCompressCss(false);
        compressor.setCompressJavaScript(false);

        compressor.setGenerateStatistics(false);

        compressor.setRemoveComments(options.isRemoveComments());
        compressor.setRemoveMultiSpaces(options.isRemoveMutliSpaces());
        compressor.setRemoveIntertagSpaces(options.isRemoveIntertagSpaces());
        compressor.setRemoveQuotes(options.isRemoveQuotes());
        compressor.setSimpleDoctype(options.isSimpleDoctype());
        compressor.setRemoveScriptAttributes(options.isRemoveScriptAttributes());
        compressor.setRemoveStyleAttributes(options.isRemoveStyleAttributes());
        compressor.setRemoveLinkAttributes(options.isRemoveLinkAttributes());
        compressor.setRemoveFormAttributes(options.isRemoveFormAttributes());
        compressor.setRemoveInputAttributes(options.isRemoveInputAttributes());
        compressor.setSimpleBooleanAttributes(options.isSimpleBooleanAttributes());
        compressor.setRemoveJavaScriptProtocol(options.isRemoveJavaScriptProtocol());
        compressor.setRemoveHttpProtocol(options.isRemoveHttpProtocol());
        compressor.setRemoveHttpsProtocol(options.isRemoveHttpsProtocol());
        compressor.setPreserveLineBreaks(options.isPreserveLineBreaks());

        return compressor.compress(src);
    }

    public void setOptions(HtmlOptions options) {
        this.options = options;
    }
}
