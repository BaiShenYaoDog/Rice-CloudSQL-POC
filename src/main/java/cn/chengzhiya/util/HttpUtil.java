package cn.chengzhiya.util;

public class HttpUtil {
    public static String getNewURL() {
        return "http://127.0.0.1:" + ConfigUtil.getConfig().getInt("serverSettings.port") + "/api/public/db/sql";
    }
}
