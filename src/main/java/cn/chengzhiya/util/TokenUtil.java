package cn.chengzhiya.util;

public class TokenUtil {
//    private static String token = null;
//
//    public static String getToken() {
//        if (token != null) {
//            return token;
//        }
//
//        StringBuilder urlBuilder = new StringBuilder("https://admin.ljxmc.top/api/public/verifySign");
//        urlBuilder.append("?").append("signVersion").append("v3");
//        try {
//            URL url = new URL(urlBuilder.toString());
//            URLConnection connection = url.openConnection();
//
//            connection.connect();
//
//            try (InputStream in = connection.getInputStream()) {
//                JSONObject jsonObject = JSONObject.parseObject(new String(in.readAllBytes()));
//                token = jsonObject.getString("token");
//                return token;
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
