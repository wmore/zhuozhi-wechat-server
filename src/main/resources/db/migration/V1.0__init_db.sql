CREATE TABLE `t_wechat_user`
(
    `id`                int(11) NOT NULL AUTO_INCREMENT,
    `city`              varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `country`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `group_id`          int(11) NOT NULL,
    `head_img_url`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `language`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `nick_name`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `open_id`           varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `province`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `qr_scene`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `qr_scene_str`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `remark`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sex`               int(11) NOT NULL,
    `subscribe`         bit(1)  NOT NULL,
    `subscribe_scene`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `subscribe_time`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `tag_id_list`       tinyblob,
    `un_subscribe_time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_wechat_user` (`open_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE `t_smart_user`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT,
    `email`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `id_number`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `open_id`          varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `school_id`        bigint(20)                              DEFAULT NULL,
    `telephone_number` varchar(11) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `user_id`          bigint(20)                              DEFAULT NULL,
    `user_name`        varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `password`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `smart_token`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_type`        int(11)    NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_smart_user` (`open_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE `t_qrcode`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `expire_seconds` int(11)    NOT NULL,
    `scene_str`      varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `ticket`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `url`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_qrcode` (`scene_str`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 24
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE `t_course_teaching`
(
    `id`            bigint(20)                              NOT NULL AUTO_INCREMENT,
    `allow_attend`  bit(1)                                  DEFAULT NULL,
    `begin_time`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `class_name`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `course_id`     bigint(20)                              NOT NULL,
    `course_name`   varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `index_img_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `lesson_url`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `qr_code_id`    bigint(20)                              DEFAULT NULL,
    `school_id`     bigint(20)                              NOT NULL,
    `snapshot_id`   bigint(20)                              NOT NULL,
    `status`        int(11)                                 DEFAULT NULL,
    `teacher_name`  varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `teaching_id`   bigint(20)                              NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_course_teaching` (`school_id`, `teaching_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 27
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

