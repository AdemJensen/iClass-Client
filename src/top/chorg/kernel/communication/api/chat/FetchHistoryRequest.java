package top.chorg.kernel.communication.api.chat;

public class FetchHistoryRequest {
    public int type;   // 1 = to class, 2 = to individual
    public int toId;   // if type = 1, then classId. if type = 2, then userId.

    public FetchHistoryRequest(int type, int toId) {
        this.type = type;
        this.toId = toId;
    }
}
