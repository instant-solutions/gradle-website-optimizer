package at.tripwire.gradle.wo;

import at.tripwire.gradle.wo.exceptions.ParseException;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser {

    private static final Logger logger = Logger.getLogger(HtmlParser.class.getName());
    private static final String TAG_REGEX = "<!--\\s*(end|)\\s*(optimize|reuse)-(css|js)\\s*([a-zA-Z0-9]*?)\\s*-->";
    private static final Pattern TAG_PATTERN = Pattern.compile(TAG_REGEX);
    private static final String CSS_REGEX = "<link.*href=\"(\\S*)\".*>";
    private static final Pattern CSS_PATTERN = Pattern.compile(CSS_REGEX);
    private static final String JS_REGEX = "<script.*src=\"(\\S*)\".*>\\s*<\\/script>";
    private static final Pattern JS_PATTERN = Pattern.compile(JS_REGEX);

    private List<File> htmlSrcFiles;

    private HtmlParser() {
        this.htmlSrcFiles = new ArrayList<File>();
    }

    public List<HtmlFile> parse() throws ParseException {
        ArrayList<HtmlFile> result = new ArrayList<HtmlFile>();

        for (File htmlSrcFile : htmlSrcFiles) {
            File htmlFolder = htmlSrcFile.getParentFile();
            String fileContent = readHtmlFile(htmlSrcFile);

            List<TagPair> tagPairs = parseTagPairs(fileContent);

            HtmlFile parsedFile = new HtmlFile();
            parsedFile.setSrcFile(htmlSrcFile);
            parsedFile.setContent(fileContent);

            for (TagPair tagPair : tagPairs) {
                String tagContent = fileContent.substring(tagPair.getContentIndexStart(), tagPair.getContentIndexEnd());

                switch (tagPair.getActionType()) {
                    case OPTIMIZE:
                        OptimizeTag optimizeTag = parseOptimizeTag(htmlFolder, tagContent, tagPair);
                        parsedFile.addOptimizeTag(optimizeTag);
                        break;
                    case REUSE:
                        ReuseTag reuseTag = parseReuseTag(tagPair);
                        parsedFile.addReuseTag(reuseTag);
                        break;
                }
            }

            result.add(parsedFile);
        }

        return result;
    }

    private ReuseTag parseReuseTag(TagPair tagPair) {
        return new ReuseTag(tagPair);
    }

    private OptimizeTag parseOptimizeTag(File rootFolder, String tagContent, TagPair tagPair) {
        OptimizeTag optimizeTag = new OptimizeTag(tagPair);

        Pattern pattern;

        switch (tagPair.getFileType()) {
            case CSS:
                pattern = CSS_PATTERN;
                break;
            case JS:
                pattern = JS_PATTERN;
                break;
            default:
                return null;
        }

        Matcher matcher = pattern.matcher(tagContent);

        while (matcher.find()) {
            optimizeTag.addSrcFile(new File(rootFolder, matcher.group(1)));
        }

        return optimizeTag;
    }

    private List<TagPair> parseTagPairs(String fileContent) throws ParseException {
        ArrayList<Tag> foundTags = new ArrayList<Tag>();

        Matcher matcher = TAG_PATTERN.matcher(fileContent);

        while (matcher.find()) {
            String endTag = matcher.group(1);
            String actionTypeTag = matcher.group(2);
            String fileTypeTag = matcher.group(3);
            String nameTag = matcher.group(4);

            Tag tag = new Tag();

            if ("css".equals(fileTypeTag)) {
                tag.fileType = TagPair.FileType.CSS;
            } else if ("js".equals(fileTypeTag)) {
                tag.fileType = TagPair.FileType.JS;
            } else {
                continue;
            }

            if ("optimize".equals(actionTypeTag)) {
                tag.actionType = TagPair.ActionType.OPTIMIZE;
            } else if ("reuse".equals(actionTypeTag)) {
                tag.actionType = TagPair.ActionType.REUSE;
            } else {
                continue;
            }

            tag.name = nameTag;
            tag.open = !"end".equals(endTag);
            tag.indexBefore = matcher.start();
            tag.indexAfter = matcher.end();

            foundTags.add(tag);
        }

        checkTagOrder(fileContent, foundTags);
        return generatePairs(foundTags);
    }

    private List<TagPair> generatePairs(List<Tag> tags) {
        ArrayList<TagPair> pairs = new ArrayList<TagPair>();

        Iterator<Tag> tagIterator = tags.iterator();

        while (tagIterator.hasNext()) {
            TagPair pair = new TagPair();
            Tag startTag = tagIterator.next();
            Tag endTag = tagIterator.next();

            pair.setName(startTag.name);
            pair.setFileType(startTag.fileType);
            pair.setActionType(startTag.actionType);
            pair.setContentIndexStart(startTag.indexAfter);
            pair.setContentIndexEnd(endTag.indexBefore);
            pairs.add(pair);
        }

        return pairs;
    }

    private void checkTagOrder(String fileContent, List<Tag> tags) throws ParseException {
        ArrayList<String> tagIds = new ArrayList<String>();
        Tag openTag = null;

        for (Tag tag : tags) {
            if (tag.open && tag.actionType == TagPair.ActionType.OPTIMIZE && tagIds.contains(tag.fileType + "-" + tag.name)) {
                String text = "Duplicate tag name \"" + tag.name + "\"!\n" + getLoggingCodePiece(fileContent, tag);
                logger.severe(text);
                throw new ParseException(text);
            }

            if (tag.open) {
                if (openTag == null) {
                    openTag = tag;
                } else {
                    String text = "No end tag found!\nStart tag: " + getLoggingCodePiece(fileContent, openTag);
                    logger.severe(text);
                    throw new ParseException(text);
                }
            } else {
                if (openTag != null) {
                    if (openTag.fileType == tag.fileType) {
                        if (openTag.actionType == tag.actionType) {
                            openTag = null;
                        } else {
                            String text = "Action types of start and end tag doesn't match!\nStart tag: " + getLoggingCodePiece(fileContent, openTag) + "\nEnd tag: " + getLoggingCodePiece(fileContent, tag);
                            logger.severe(text);
                            throw new ParseException(text);
                        }
                    } else {
                        String text = "File types of start and end tag doesn't match!\nStart tag: " + getLoggingCodePiece(fileContent, openTag) + "\nEnd tag: " + getLoggingCodePiece(fileContent, tag);
                        logger.severe(text);
                        throw new ParseException(text);
                    }
                } else {
                    logger.severe("End tag found before any start tag!");
                    throw new ParseException("End tag found before any start tag!");
                }
            }

            if (tag.actionType == TagPair.ActionType.OPTIMIZE && tag.open) {
                tagIds.add(tag.fileType + "-" + tag.name);
            }
        }
    }

    private String getLoggingCodePiece(String fileContent, Tag tag) {
        return fileContent.substring(tag.indexBefore, tag.indexAfter);
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

        private TagPair.FileType fileType;
        private TagPair.ActionType actionType;
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
            this.instance.htmlSrcFiles.add(htmlFile);
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
