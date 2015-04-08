package at.tripwire.gradle.wo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MinifyEntry {

    public enum Type {
        JS, CSS
    }

    private String name;
    private List<File> srcFiles;
    private Type type;

    public MinifyEntry() {
        this.srcFiles = new ArrayList<File>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<File> getSrcFiles() {
        return srcFiles;
    }

    public void addSrcFile(File srcFile) {
        srcFiles.add(srcFile);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
