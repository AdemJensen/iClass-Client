package top.chorg.kernel.communication.api.announcements;

public class FetchListRequest {
    public int classId;
    public int level;
    public int publisherId;

    public FetchListRequest(int classId, int level, int publisherId) {
        this.classId = classId;
        this.level = level;
        this.publisherId = publisherId;
    }
}
