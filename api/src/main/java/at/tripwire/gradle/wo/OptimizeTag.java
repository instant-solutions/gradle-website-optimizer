package at.tripwire.gradle.wo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OptimizeTag {

    private List<File> srcFiles;
    private TagPair srcTag;

    public OptimizeTag(TagPair tagPair) {
        this.srcTag = tagPair;
        this.srcFiles = new ArrayList<File>();
    }

    public List<File> getSrcFiles() {
        return srcFiles;
    }

    public void addSrcFile(File srcFile) {
        srcFiles.add(srcFile);
    }

    public TagPair getSrcTag() {
        return srcTag;
    }
}
