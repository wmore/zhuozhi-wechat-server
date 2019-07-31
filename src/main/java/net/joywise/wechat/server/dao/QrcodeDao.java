package net.joywise.wechat.server.dao;

import net.joywise.wechat.server.bean.db.QrCode;
import net.joywise.wechat.server.bean.db.SmartUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface QrcodeDao extends CrudRepository<QrCode, Long> {

    @Query(value = "select * from t_qrcode where scene_str = ?1", nativeQuery = true)
    QrCode queryByScene(String scene);

}
