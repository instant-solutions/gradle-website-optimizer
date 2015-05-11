package at.tripwire.gradle.wo;

import at.tripwire.gradle.wo.tags.TagPair;
import org.gradle.api.Project;

import java.io.File;

public class Utils {

    public static File getDestinationFile(Project project, TagPair tag) {
        File dir = null;
        String fileSuffix = null;

        switch (tag.getFileType()) {
            case CSS:
                dir = getCssOutputDir(project);
                fileSuffix = ".min.css";
                break;
            case JS:
                dir = getJsOutputDir(project);
                fileSuffix = ".min.js";
                break;
        }

        return new File(dir, tag.getName() + fileSuffix);
    }

    public static File getDestinationFile(Project project, HtmlFile htmlFile) {
        return new File(getOutputDir(project), htmlFile.getSrcFile().getName());
    }

    private static File getJsOutputDir(Project project) {
        return new File(getOutputDir(project), "js");
    }

    private static File getCssOutputDir(Project project) {
        return new File(getOutputDir(project), "css");
    }

    public static File getOutputDir(Project project) {
        return new File(project.getBuildDir(), "output");
    }

    public static String getRelativeDestinationPath(Project project, TagPair tag) {
        File destFile = getDestinationFile(project, tag);
        File rootFolder = getOutputDir(project);

        return rootFolder.toURI().relativize(destFile.toURI()).getPath();
    }
}
