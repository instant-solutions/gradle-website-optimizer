package at.tripwire.gradle.wo;

import at.tripwire.gradle.wo.options.HtmlOptions;
import groovy.lang.Closure;
import org.gradle.api.file.FileCollection;
import org.gradle.util.ConfigureUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class WebsiteProject {

    private Collection<File> srcFiles;
    private HtmlOptions htmlOptions;

    public WebsiteProject() {
        this.srcFiles = new ArrayList<>();
    }

    public void html(File srcFile) {
        srcFiles.add(srcFile);
    }

    public void html(FileCollection fileCollection) {
        srcFiles.addAll(fileCollection.getFiles());
    }

    public void htmlOptions(Closure closure) {
        this.htmlOptions = new HtmlOptions();
        ConfigureUtil.configure(closure, htmlOptions);
    }

    public Collection<File> getSrcFiles() {
        return srcFiles;
    }

    public HtmlOptions getHtmlOptions() {
        return htmlOptions;
    }
}
