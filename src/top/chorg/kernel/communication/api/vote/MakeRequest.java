package top.chorg.kernel.communication.api.vote;

public class MakeRequest {
    public int voteId;
    public int[] ops;

    public MakeRequest(int voteId, int[] ops) {
        this.voteId = voteId;
        this.ops = ops;
    }
}
