package net.joywise.wechat.server.dao;

import net.joywise.wechat.server.bean.db.SmartUser;
import net.joywise.wechat.server.bean.db.WechatUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface SmartUserDao extends CrudRepository<SmartUser, Long> {

    @Query(value = "select * from t_smart_user where open_id = ?1", nativeQuery = true)
    SmartUser queryByOpenId(String openId);

    @Query(value = "select * from t_smart_user where user_id = ?1 and school_id = ?2", nativeQuery = true)
    SmartUser queryByUserIdAndSchoolId(Long userId, Long schoolId);

    @Query(value = "select * from t_smart_user where user_name = ?1 and school_id = ?2", nativeQuery = true)
    SmartUser queryByUserNameAndSchoolId(String userName, Long schoolId);

}
