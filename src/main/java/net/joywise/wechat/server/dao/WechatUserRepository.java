package net.joywise.wechat.server.dao;

import net.joywise.wechat.server.bean.db.WechatUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface WechatUserRepository extends CrudRepository<WechatUser, Long> {

    @Query(value = "select * from t_wechat_user where open_id = ?1", nativeQuery = true)
    WechatUser queryByOpenId(String openId);

}
