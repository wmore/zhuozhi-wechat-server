package net.joywise.wechat.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.joywise.wechat.server.bean.db.CourseTeaching;
import net.joywise.wechat.server.bean.db.QrCode;
import net.joywise.wechat.server.dao.CourseTeachingDao;
import net.joywise.wechat.server.error.CommonException;
import net.joywise.wechat.server.service.CourseTeachingService;
import net.joywise.wechat.server.service.QrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Service
public class CourseTeachingServiceImpl implements CourseTeachingService {

    @Autowired
    CourseTeachingDao courseTeachingDao;

    @Autowired
    QrcodeService qrcodeService;

    @Value("${com.constant.weixin.appId}")
    public String appId;//自己在微信测试平台设置的appId

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
        try {
            long schoolId = courseTeaching.getSchoolId();
            long snapshotId = courseTeaching.getSnapshotId();

            CourseTeaching courseExists = courseTeachingDao.queryBySnapshotId(snapshotId, schoolId);
            if (courseExists != null) {
                courseTeaching = courseExists;
            }

            try {
                String redirctUrl = "http://wyuetest.natapp1.cc/h5api/cloud/index.html";
                redirctUrl = URLEncoder.encode(redirctUrl, "utf-8");
                String lessonUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURI&response_type=code&scope=snsapi_base&state=SNAPSHOTID#wechat_redirect";
                lessonUrl = lessonUrl.replace("APPID", appId);
                lessonUrl = lessonUrl.replace("REDIRECTURI", String.valueOf(URLEncoder.encode(redirctUrl, "utf-8")));
                lessonUrl = lessonUrl.replace("SNAPSHOTID", String.valueOf(snapshotId));

                courseExists.setLessonUrl(lessonUrl);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String sceneStr = courseTeaching.getSchoolId() + "_" + courseTeaching.getSnapshotId();
            QrCode qrCode = qrcodeService.getQrTicketWithScene(sceneStr);
            courseTeaching.setQrCodeId(qrCode.getId());
            CourseTeaching course = courseTeachingDao.save(courseTeaching);
            course.setQrCode(qrCode);
            return course;
        } catch (Exception e) {
            throw new CommonException("创建上课通知失败！原因:" + e.getMessage());
        }
    }


}
