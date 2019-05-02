package top.chorg.kernel.communication.api.file;

import top.chorg.support.DateTime;

import java.math.BigDecimal;

public class FileInfo {
    public int id;
    public String name;
    public int uploader;
    public DateTime date;
    public int classId, level;
    public BigDecimal size;

    public FileInfo(int id, String name, int uploader, DateTime date, int classId, int level, BigDecimal size) {
        this.id = id;
        this.name = name;
        this.uploader = uploader;
        this.date = date;
        this.classId = classId;
        this.level = level;
        this.size = size;
    }
}
