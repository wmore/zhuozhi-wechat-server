package net.joywise.wechat.server.bean.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author: wyue
 * @Date: 2019/7/31 18:41
 * @Description:
 */
@Data
@Entity
@Table(name = "t_course_teaching", uniqueConstraints = {@UniqueConstraint(columnNames = {"schoolId", "snapshotId"})})
public class CourseTeaching {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    //授课教师名称
    @ApiModelProperty(value = "授课教师名称", required = true)
    @NotNull(message = "teacherName能为空")
    private String teacherName;

    //快照ID
    @ApiModelProperty(value = "快照ID", required = true)
    @NotNull(message = "snapshotId能为空")
    private Long snapshotId;

    //授课ID
    @ApiModelProperty(value = "授课ID", required = true)
    @NotNull(message = "teachingId能为空")
    private Long teachingId;

    //课程ID
    @ApiModelProperty(value = "课程ID", required = true)
    @NotNull(message = "courseId能为空")
    private Long courseId;

    //课程名称
    @ApiModelProperty(value = "课程名称", required = true)
    @NotNull(message = "courseName能为空")
    private String courseName;

    //上课时间
    @ApiModelProperty(value = "上课时间", required = true)
    @NotNull(message = "beginTime能为空")
    private String beginTime;

    //班级名称
    @ApiModelProperty(value = "班级名称", required = true)
    @NotNull(message = "className能为空")
    private String className;

    //所属机构
    @ApiModelProperty(value = "所属机构", required = true)
    @NotNull(message = "schoolId能为空")
    private Long schoolId;

    //上课状态 1-正在上课 2-上课结束
    @ApiModelProperty(value = "上课状态 1-正在上课 2-上课结束")
    private Integer status;

    //是否允许旁听生 true--允许 false-不允许
    @ApiModelProperty(value = "是否允许旁听生 true--允许 false-不允许")
    private Boolean allowAttend;

    // 课程h5地址
    @ApiModelProperty(value = "课程h5地址")
    private String lessonUrl;

    // 课程图片地址
    @ApiModelProperty(value = "课程图片地址")
    private String indexImgUrl;

    private Long qrCodeId;

    public CourseTeaching() {
    }

    @Transient
    private QrCode qrCode;
}
