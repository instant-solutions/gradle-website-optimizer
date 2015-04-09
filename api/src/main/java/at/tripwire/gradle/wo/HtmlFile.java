package at.tripwire.gradle.wo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HtmlFile {

    private File srcFile;
    private String content;
    private List<OptimizeTag> optimizeTags = new ArrayList<OptimizeTag>();
    private List<ReuseTag> reuseTags = new ArrayList<ReuseTag>();

    public File getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<OptimizeTag> getOptimizeTags() {
        return optimizeTags;
    }

    public void addOptimizeTag(OptimizeTag tag) {
        this.optimizeTags.add(tag);
    }

    public List<ReuseTag> getReuseTags() {
        return reuseTags;
    }

    public void addReuseTag(ReuseTag tag) {
        this.reuseTags.add(tag);
    }
}
