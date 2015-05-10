package at.tripwire.gradle.wo.tasks;

import at.tripwire.gradle.wo.HtmlFile;
import at.tripwire.gradle.wo.Utils;
import at.tripwire.gradle.wo.optimizer.HtmlProcessor;
import at.tripwire.gradle.wo.tags.OptimizeTag;
import at.tripwire.gradle.wo.tags.ReuseTag;
import at.tripwire.gradle.wo.tags.TagPair;
import org.apache.commons.io.IOUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OptimizeHtmlTask extends DefaultTask {

    private File destFile;
    private HtmlFile srcFile;

    @TaskAction
    public void optimize() {
        String content = srcFile.getContent();

        Map<TagPair, String> replaceTexts = new HashMap<>();

        for (OptimizeTag optimizeTag : srcFile.getOptimizeTags()) {
            TagPair tagPair = optimizeTag.getSrcTag();
            cacheReplaceText(content, tagPair, replaceTexts);
        }

        for (ReuseTag reuseTag : srcFile.getReuseTags()) {
            TagPair tagPair = reuseTag.getSrcTag();
            cacheReplaceText(content, tagPair, replaceTexts);
        }

        for (TagPair pair : replaceTexts.keySet()) {
            String replaceText = replaceTexts.get(pair);
            content = content.replace(replaceText, getHtmlInclude(pair));
        }

        HtmlProcessor processor = new HtmlProcessor();
        String result = processor.process(content);

        try (FileOutputStream fileOut = new FileOutputStream(destFile)) {
            IOUtils.write(result, new BufferedOutputStream(fileOut));
        } catch (IOException e) {
            throw new GradleScriptException("Failed to write processed HTML file!", e);
        }

    }

    private void cacheReplaceText(String content, TagPair tagPair, Map<TagPair, String> replaceTexts) {
        String replaceText = content.substring(tagPair.getContentIndexStart(), tagPair.getContentIndexEnd());
        replaceTexts.put(tagPair, replaceText);
    }

    private String getHtmlInclude(TagPair pair) {
        StringBuilder builder = new StringBuilder();

        switch (pair.getFileType()) {
            case CSS: // <link rel="stylesheet" href="stylesheet.css" type="text/css">
                builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
                builder.append(Utils.getRelativeDestinationPath(getProject(), pair));
                builder.append("\" />");
                break;
            case JS:
                builder.append("<script type=\"text/javascript\" src=\"");
                builder.append(Utils.getRelativeDestinationPath(getProject(), pair));
                builder.append("\"></script>");
                break;
        }


        return builder.toString();
    }

    public void setSrcFile(HtmlFile srcFile) {
        this.srcFile = srcFile;
    }

    public void setDestFile(File destFile) {
        this.destFile = destFile;
    }
}
