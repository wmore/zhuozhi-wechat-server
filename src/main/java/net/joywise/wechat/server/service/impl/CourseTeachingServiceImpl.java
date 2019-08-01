package net.joywise.wechat.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.CourseTeaching;
import net.joywise.wechat.server.bean.db.QrCode;
import net.joywise.wechat.server.dao.CourseTeachingDao;
import net.joywise.wechat.server.service.CourseTeachingService;
import net.joywise.wechat.server.service.QrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CourseTeachingServiceImpl implements CourseTeachingService {

    @Autowired
    CourseTeachingDao courseTeachingDao;

    @Autowired
    QrcodeService qrcodeService;

    @Override
    public void save(CourseTeaching courseTeaching) {
        courseTeachingDao.save(courseTeaching);
    }

    @Override
    public CourseTeaching queryBySnapshotId(long snapshotId, long schoolId) {
        return courseTeachingDao.queryBySnapshotId(snapshotId, schoolId);
    }

    @Override
    public CourseTeaching queryByTeachingId(long teachingId, long schoolId) {
        return courseTeachingDao.queryByTeachingId(teachingId, schoolId);
    }

    @Override
    public CourseTeaching queryByCourseId(long courseId, long schoolId) {
        return courseTeachingDao.queryByCourseId(courseId, schoolId);
    }

    @Transactional
    @Override
    public CourseTeaching createNewCourse(CourseTeaching courseTeaching) {
        // scene 格式为   schoolId_snapshotId
        String sceneStr = courseTeaching.getSchoolId() + "_" + courseTeaching.getSnapshotId();
        QrCode qrCode = qrcodeService.getQrTicketWithScene(sceneStr);
        courseTeaching.setQrCodeId(qrCode.getId());
        CourseTeaching course = courseTeachingDao.save(courseTeaching);
        course.setQrCode(qrCode);
        return course;
    }
}
