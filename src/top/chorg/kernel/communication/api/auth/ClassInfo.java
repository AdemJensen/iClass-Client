package top.chorg.kernel.communication.api.auth;

import top.chorg.support.DateTime;

public class ClassInfo {
    public int id, avatar;
    public String name, introduction;
    public DateTime date;
    public int[] classmates;

    public ClassInfo(int id, int avatar, String name, String introduction, DateTime date, int[] classmates) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.introduction = introduction;
        this.date = date;
        this.classmates = classmates;
    }
}
