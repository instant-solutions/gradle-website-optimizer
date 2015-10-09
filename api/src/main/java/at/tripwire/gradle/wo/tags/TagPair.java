package at.tripwire.gradle.wo.tags;

public class TagPair {

    public enum FileType {
        CSS, JS
    }

    public enum ActionType {
        OPTIMIZE, REUSE
    }

    public enum LocationType {
        FILE, INCLUDE
    }

    private String name;
    private FileType fileType;
    private ActionType actionType;
    private LocationType locationType;
    private int contentIndexStart;
    private int contentIndexEnd;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public int getContentIndexStart() {
        return contentIndexStart;
    }

    public void setContentIndexStart(int contentIndexStart) {
        this.contentIndexStart = contentIndexStart;
    }

    public int getContentIndexEnd() {
        return contentIndexEnd;
    }

    public void setContentIndexEnd(int contentIndexEnd) {
        this.contentIndexEnd = contentIndexEnd;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TagPair{");
        sb.append("name='").append(name).append('\'');
        sb.append(", fileType=").append(fileType);
        sb.append(", actionType=").append(actionType);
        sb.append(", locationType=").append(locationType);
        sb.append(", contentIndexStart=").append(contentIndexStart);
        sb.append(", contentIndexEnd=").append(contentIndexEnd);
        sb.append('}');
        return sb.toString();
    }
}
