package net.joywise.wechat.server.bean.wechat.message;

/**
 * @Title: News
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wyue
 * @date: 2019/8/1 8:43
 * @最后修改人: Administrator
 * @最后修改时间: 2019/8/1 8:43
 * @company: shopin.net
 * @version: V1.0
 */
public class News{
    private String Title;

    private String Description;

    private String PicUrl;

    private String Url;

    public News() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
