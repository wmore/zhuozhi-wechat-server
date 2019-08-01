package net.joywise.wechat.server.bean.db;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author: wyue
 * @Date: 2019/7/31 18:41
 * @Description:
 */
@Data
@Entity
@Table(name = "t_course_teaching", uniqueConstraints = {@UniqueConstraint(columnNames = {"schoolId", "teachingId"})})
public class CourseTeaching {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    //授课教师名称
    @NotNull(message = "teacherName能为空")
    private String teacherName;

    //快照ID
    @NotNull(message = "snapshotId能为空")
    private Long snapshotId;

    //授课ID
    @NotNull(message = "teachingId能为空")
    private Long teachingId;

    //课程ID
    @NotNull(message = "courseId能为空")
    private Long courseId;

    //课程名称
    @NotNull(message = "courseName能为空")
    private String courseName;

    //上课时间
    @NotNull(message = "beginTime能为空")
    private String beginTime;

    //班级名称
    @NotNull(message = "className能为空")
    private String className;

    //所属机构
    @NotNull(message = "schoolId能为空")
    private Long schoolId;

    //上课状态 1-正在上课 2-上课结束
    private Integer status;

    //是否允许旁听生 true--允许 false-不允许
    private Boolean allowAttend;

    // 课程h5地址
    private String lessonUrl;

    // 课程图片地址
    private String indexImgUrl;

    private Long qrCodeId;

    public CourseTeaching() {
    }

    @Transient
    private QrCode qrCode;
}
