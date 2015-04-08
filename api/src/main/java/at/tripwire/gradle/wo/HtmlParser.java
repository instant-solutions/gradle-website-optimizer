package at.tripwire.gradle.wo;

import at.tripwire.gradle.wo.exceptions.ParseException;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser {

    private static final Logger logger = Logger.getLogger(HtmlParser.class.getName());

    private static final String FLAG_MINIFY_JS = "optimize-js";
    private static final String FLAG_MINIFY_CSS = "optimize-css";
    private static final String FLAG_REUSE_CSS = "reuse-css";
    private static final String FLAG_REUSE_JS = "reuse-js";

    private static final String REGEX = "<!--\\s*(end|)\\s*(optimize|reuse)-(css|js)\\s*([a-zA-Z0-9]*?)\\s*-->";

    private List<File> htmlFiles;

    private HtmlParser() {
        this.htmlFiles = new ArrayList<File>();
    }

    public void parse() throws ParseException {
        Pattern pattern = Pattern.compile(REGEX);

        for (File htmlFile : htmlFiles) {
            String fileContent = readHtmlFile(htmlFile);

            ArrayList<Tag> foundTags = new ArrayList<Tag>();

            Matcher matcher = pattern.matcher(fileContent);

            while (matcher.find()) {
                String endTag = matcher.group(1);
                String actionTypeTag = matcher.group(2);
                String fileTypeTag = matcher.group(3);
                String nameTag = matcher.group(4);

                Tag tag = new Tag();

                if("css".equals(fileTypeTag)) {
                    tag.fileType = Tag.FileType.CSS;
                } else if("js".equals(fileTypeTag)) {
                    tag.fileType = Tag.FileType.JS;
                } else {
                    continue;
                }

                if("optimize".equals(actionTypeTag)) {
                    tag.actionType = Tag.ActionType.OPTIMIZE;
                } else if("reuse".equals(actionTypeTag)) {
                    tag.actionType = Tag.ActionType.REUSE;
                } else {
                    continue;
                }

                tag.name = nameTag;
                tag.open = !"end".equals(endTag);
                tag.indexBefore = matcher.start();
                tag.indexAfter = matcher.end();

                System.out.println(tag);

                foundTags.add(tag);
            }

        }

    }

    private String readHtmlFile(File file) throws ParseException {
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(file);
            return IOUtils.toString(fileIn);
        } catch (FileNotFoundException e) {
            String msg = "HTML file couldn't be found!";
            logger.log(Level.SEVERE, msg, e);
            throw new ParseException(msg, e);
        } catch (IOException e) {
            String msg = "Failed reading HTML file!";
            logger.log(Level.SEVERE, msg, e);
            throw new ParseException(msg, e);
        } finally {
            IOUtils.closeQuietly(fileIn);
        }
    }

    private static class Tag {

        public enum FileType {
            CSS, JS
        }

        public enum ActionType {
            OPTIMIZE, REUSE
        }

        private FileType fileType;
        private ActionType actionType;
        private boolean open;
        private String name;
        private int indexBefore;
        private int indexAfter;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Tag{");
            sb.append("fileType=").append(fileType);
            sb.append(", actionType=").append(actionType);
            sb.append(", open=").append(open);
            sb.append(", name='").append(name).append('\'');
            sb.append(", indexBefore=").append(indexBefore);
            sb.append(", indexAfter=").append(indexAfter);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Builder {

        private HtmlParser instance;

        public Builder() {
            this.instance = new HtmlParser();
        }

        public Builder add(File htmlFile) {
            this.instance.htmlFiles.add(htmlFile);
            return this;
        }

        public HtmlParser build() {
            return instance;
        }
    }


    public static void main(String[] args) throws ParseException {
        new HtmlParser.Builder().add(new File("C:\\Users\\david.MKW-W\\Desktop\\website\\index.html")).build().parse();
    }
}
