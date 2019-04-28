package top.chorg.kernel.communication.api.file;

public class DownloadRequestReturn {
    public int id;
    public String token;

    public DownloadRequestReturn(int id, String token) {
        this.id = id;
        this.token = token;
    }
}
