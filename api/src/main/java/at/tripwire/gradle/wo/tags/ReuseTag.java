package at.tripwire.gradle.wo.tags;

public class ReuseTag {

    private TagPair srcTag;

    public ReuseTag(TagPair tagPair) {
        this.srcTag = tagPair;
    }

    public TagPair getSrcTag() {
        return srcTag;
    }
}
