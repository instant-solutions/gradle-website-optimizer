package at.tripwire.gradle.wo;

import org.gradle.api.file.FileCollection;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class WebsiteProject {

    private Collection<File> srcFiles;

    public WebsiteProject() {
        this.srcFiles = new ArrayList<>();
    }

    public void html(File srcFile) {
        srcFiles.add(srcFile);
    }

    public void html(FileCollection fileCollection) {
        srcFiles.addAll(fileCollection.getFiles());
    }

    public Collection<File> getSrcFiles() {
        return srcFiles;
    }
}
