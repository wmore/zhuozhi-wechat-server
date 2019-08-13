package net.joywise.wechat.server.enums;

import lombok.Getter;

@Getter
public enum UserType {
    /***
     * 学生
     */
    STUDENT(0),
    /***
     * 教师
     */
    TEACHER(1),
    /***
     * 平台用户
     */
    PLATFORM_USER(2),
    /***
     * 超级管理员
     */
    ADMINISTRATOR(3);

    /**
     * type代码
     */
    private int type;

    UserType(int type) {
        this.type = type;
    }
}
