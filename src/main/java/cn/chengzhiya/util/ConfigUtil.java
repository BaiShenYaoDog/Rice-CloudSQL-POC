package cn.chengzhiya.util;

import cn.chengzhiya.config.YamlConfiguration;

import java.io.File;

public class ConfigUtil {
    private static final File configFile = new File("config.yml");
    private static YamlConfiguration config;

    public static void saveDefaultConfig() {
        FileUtil.saveResource("config.yml", "config.yml", false);
    }

    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static YamlConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }
}
