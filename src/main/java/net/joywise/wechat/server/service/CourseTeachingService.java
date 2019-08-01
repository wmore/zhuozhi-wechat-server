package net.joywise.wechat.server.service;

import net.joywise.wechat.server.bean.db.CourseTeaching;

public interface CourseTeachingService {

    void save(CourseTeaching courseTeaching);

    CourseTeaching queryBySnapshotId(long snapshotId, long schoolId);

    CourseTeaching queryByTeachingId(long teachingId, long schoolId);

    CourseTeaching queryByCourseId(long courseId, long schoolId);

    CourseTeaching createNewCourse(CourseTeaching courseTeaching);
}
