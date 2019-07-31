package net.joywise.wechat.server.dao;

import net.joywise.wechat.server.bean.db.CourseTeaching;
import net.joywise.wechat.server.bean.db.WechatUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface CourseTeachingDao extends CrudRepository<CourseTeaching, Long> {

    @Query(value = "select * from t_course_teaching where snapshot_id = ?1 and school_id = ?2", nativeQuery = true)
    CourseTeaching queryBySnapshotId(long snapshotId, long schoolId);

    @Query(value = "select * from t_course_teaching where teaching_id = ?1 and school_id = ?2", nativeQuery = true)
    CourseTeaching queryByTeachingId(long teachingId, long schoolId);

    @Query(value = "select * from t_course_teaching where course_id = ?1 and school_id = ?2", nativeQuery = true)
    CourseTeaching queryByCourseId(long courseId, long schoolId);

}
