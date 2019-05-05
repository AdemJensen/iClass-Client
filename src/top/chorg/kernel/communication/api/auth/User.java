package top.chorg.kernel.communication.api.auth;

import top.chorg.support.Date;
import top.chorg.support.DateTime;

public class User {

    private int id, sex, grade, avatar;
    private int[] classId;
    private String username, realName, nickname, email, phone;
    private Date birthday;
    private DateTime regTime;
    private char userGroup;

    public User(int id, int[] classId, int sex, int grade, int avatar, String username, String realName,
                String nickname, String email, String phone, Date birthday, DateTime regTime, char userGroup) {
        this.assign(id, classId, sex, grade, avatar,
                username, realName, nickname, email, phone, birthday, regTime, userGroup);
    }

    public void assign(int id, int[] classId, int sex, int grade, int avatar, String username, String realName,
                       String nickname, String email, String phone, Date birthday, DateTime regTime, char userGroup) {
        this.id = id;
        this.classId = classId;
        this.sex = sex;
        this.grade = grade;
        this.avatar = avatar;
        this.username = username;
        this.realName = realName;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.regTime = regTime;
        this.userGroup = userGroup;
    }

    public void assign(User alt) {
        this.assign(
                alt.id,
                alt.classId,
                alt.sex,
                alt.grade,
                alt.avatar,
                alt.username,
                alt.realName,
                alt.nickname,
                alt.email,
                alt.phone,
                alt.birthday,
                alt.regTime,
                alt.userGroup
        );
    }

    public int getId() {
        return id;
    }

    public int[] getClassId() {
        return classId;
    }

    public boolean isInClass(int classId) {
        for (int id : this.classId) {
            if (id == classId) return true;
        }
        return false;
    }

    public int getSex() {
        return sex;
    }

    public int getGrade() {
        return grade;
    }

    public int getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return this.username;
    }

    public String getRealName() {
        return realName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public DateTime getRegTime() {
        return regTime;
    }

    public char getUserGroup() {
        return userGroup;
    }
}
