package at.tripwire.gradle.wo;

import at.tripwire.gradle.wo.exceptions.ParseException;
import at.tripwire.gradle.wo.exceptions.ProcessException;
import at.tripwire.gradle.wo.optimizer.CssProcessor;
import at.tripwire.gradle.wo.optimizer.JsProcessor;
import at.tripwire.gradle.wo.tags.OptimizeTag;
import at.tripwire.gradle.wo.tags.TagPair;

import java.io.File;
import java.util.List;

public class Test {

    public static void main(String[] args) throws ParseException, ProcessException {
        List<HtmlFile> htmlFiles = new HtmlParser.Builder().add(new File("C:\\Users\\david.MKW-W\\Desktop\\website\\index.html")).build().parse();

        JsProcessor processor = new JsProcessor();
        CssProcessor cssProcessor = new CssProcessor();

        for (HtmlFile htmlFile : htmlFiles) {
            for (OptimizeTag optimizeTag : htmlFile.getOptimizeTags()) {
                if (optimizeTag.getSrcTag().getFileType() == TagPair.FileType.JS) {
                    System.out.println(processor.process(optimizeTag.getSrcFiles().toArray(new File[0])));
                } else if (optimizeTag.getSrcTag().getFileType() == TagPair.FileType.CSS) {
                    System.out.println(cssProcessor.process(optimizeTag.getSrcFiles().toArray(new File[0])));
                }
            }
        }
    }
}
