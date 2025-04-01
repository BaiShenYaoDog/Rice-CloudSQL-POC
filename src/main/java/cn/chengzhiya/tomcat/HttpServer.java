package cn.chengzhiya.tomcat;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class HttpServer extends HttpServlet {
    public String fakeSql = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getRequestURI();
//        System.out.println("请求GET-API: " + path);

//        StringBuilder paramsBuilder = new StringBuilder();
//        req.getParameterMap().keySet()
//                .forEach(key -> paramsBuilder.append(key).append("=").append(req.getParameter(key)).append(", "));
//        System.out.println("请求参数: " + paramsBuilder);

        switch (path) {
            case "/api/public/db/fake" -> {
                fakeSql = req.getParameter("sql");

                PrintWriter out = resp.getWriter();
                out.write("OK");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getRequestURI();
//        System.out.println("请求POST-API: " + path);

        switch (path) {
            case "/api/public/db/sql" -> {
                StringBuilder bodyBuilder = new StringBuilder();
                req.getReader().lines().forEach(bodyBuilder::append);
//                System.out.println("请求数据: " + bodyBuilder);

                JSONObject body = JSONObject.parseObject(bodyBuilder.toString());
                JSONObject dbSql = body.getJSONObject("dbSql");

                StringBuilder sqlBuilder = new StringBuilder();
                if (fakeSql == null) {
                    switch (body.getString("dbType")) {
                        case "selectCountSql" -> {
                            sqlBuilder.append("SELECT").append(" ");
                            sqlBuilder.append("COUNT(*)").append(" ");
                            sqlBuilder.append("FROM").append(" ").append(dbSql.getString("tableName"));
                            sqlBuilder.append(dbSql.getString("where"));
                        }
                        case "selectDataSql" -> {
                            sqlBuilder.append("SELECT").append(" ");
                            sqlBuilder.append(dbSql.getString("field")).append(" ");
                            sqlBuilder.append("FROM").append(" ").append(dbSql.getString("tableName"));
                            sqlBuilder.append(dbSql.getString("where"));
                        }
                        case "insertDataSql" -> {
                            sqlBuilder.append("INSERT INTO").append(" ");
                            sqlBuilder.append(dbSql.getString("tableName"));
                            sqlBuilder.append("(").append(dbSql.getString("field")).append(")").append(" ");
                            sqlBuilder.append("VALUES").append(" ");
                            int fieldInfoMapSize = dbSql.getInteger("fieldInfoMap");
                            for (int i = 0; i < fieldInfoMapSize; i++) {
                                sqlBuilder.append("?");
                                if (i < fieldInfoMapSize - 1) {
                                    sqlBuilder.append(",");
                                }
                            }
                        }
                        case "updateDataSql" -> {
                            sqlBuilder.append("UPDATE").append(" ");
                            sqlBuilder.append(dbSql.getString("tableName")).append(" ");
                            sqlBuilder.append("SET").append(" ");
                            List<String> updateFieldList = dbSql.getList("updatefieldList", String.class);
                            for (int i = 0; i < updateFieldList.size(); i++) {
                                sqlBuilder.append(updateFieldList.get(i));
                                if (i != updateFieldList.size() - 1) {
                                    sqlBuilder.append(", ");
                                }
                            }
                            sqlBuilder.append(dbSql.getString("where"));
                        }
                        case "deleteDataSql" -> {
                            sqlBuilder.append("DELETE FROM").append(" ");
                            sqlBuilder.append(dbSql.getString("tableName"));
                            sqlBuilder.append(dbSql.getString("where"));
                        }
                    }
                } else {
//                    System.out.println("已发送虚假SQL");
                    sqlBuilder.append(fakeSql);
                }

                PrintWriter out = resp.getWriter();
                out.write(sqlBuilder.toString());
            }
        }
    }
}
