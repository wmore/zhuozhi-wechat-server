package net.joywise.wechat.server.dao;

import net.joywise.wechat.server.bean.db.School;
import net.joywise.wechat.server.bean.db.SmartUser;
import net.joywise.wechat.server.bean.vo.SchoolVo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface SchoolDao extends CrudRepository<School, Long> {

    @Query(value = "select new net.joywise.wechat.server.bean.vo.SchoolVo(s.schoolId, s.schoolName, s.domain, s.schoolType,s.isWechatUsed, e.domainAddressMapper, e.socketIOAddressMapper) " +
            "from School s LEFT JOIN SchoolEdition e on s.schoolId = e.schoolId " +
            "where s.isWechatUsed = ?1")
    List<SchoolVo> getSchoolList(int isWechatUsed);


    @Query(value = "select new net.joywise.wechat.server.bean.vo.SchoolVo(s.schoolId, s.schoolName, s.domain, s.schoolType,s.isWechatUsed, e.domainAddressMapper, e.socketIOAddressMapper) " +
            "from School s LEFT JOIN SchoolEdition e on s.schoolId = e.schoolId " +
            "where s.schoolId = ?1")
    SchoolVo getSchoolById(long schoolId);


}
