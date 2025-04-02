package cn.chengzhiya;

import cn.chengzhiya.tomcat.TomcatServer;
import cn.chengzhiya.transformer.title.DbConstantTransformer;
import cn.chengzhiya.transformer.title.DbHttpUtilTransformer;
import cn.chengzhiya.transformer.title.SignUtilTransformer;
import cn.chengzhiya.util.ConfigUtil;
import lombok.Getter;

import java.lang.instrument.Instrumentation;

public class Agent {
    @Getter
    private static TomcatServer tomcatServer;

    public static void premain(String args, Instrumentation inst) {
        ConfigUtil.saveDefaultConfig();
        ConfigUtil.reloadConfig();

//        TokenUtil.getToken();

        if (ConfigUtil.getConfig().getBoolean("serverSettings.enable")) {
            tomcatServer = new TomcatServer();
            tomcatServer.startServer(ConfigUtil.getConfig().getInt("serverSettings.port"));
        }

        inst.addTransformer(new DbHttpUtilTransformer());
        inst.addTransformer(new SignUtilTransformer());
        inst.addTransformer(new DbConstantTransformer());
    }
}
