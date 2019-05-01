package top.chorg.kernel.communication.api.auth;

import top.chorg.support.Date;

public class AlterUserRequest {
    public int sex, grade, avatar;
    public String realName, nickname, email, phone;
    public Date birthday;

    public AlterUserRequest(int sex, int grade, int avatar, String realName, String nickname,
                            String email, String phone, Date birthday) {
        this.sex = sex;
        this.grade = grade;
        this.avatar = avatar;
        this.realName = realName;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }
}
