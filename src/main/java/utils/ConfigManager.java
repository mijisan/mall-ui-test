package utils;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 配置管理器，负责加载运行时配置。
 */
@Slf4j
public class ConfigManager {

    /**
     * 加载配置，优先级：JVM 启动参数(-D) > 环境变量(APP_*) > .env 文件 > config.properties
     */
    public Properties initProp() {
        Properties prop = new Properties();

        // 1. 从 classpath 加载 config.properties（基础配置 + 回退默认值）
        try (InputStream inputStream = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config/config.properties")) {
            if (inputStream != null) {
                prop.load(inputStream);
                log.info("配置文件加载成功: config.properties");
            } else {
                log.warn("未找到配置文件 config.properties，将使用默认配置");
            }
        } catch (Exception e) {
            log.error("加载配置文件失败", e);
        }

        // 2. 从项目根目录 .env 文件加载（覆盖 config.properties 中的同名属性）
        Path envPath = Paths.get(".env");
        if (Files.exists(envPath)) {
            try (InputStream envStream = Files.newInputStream(envPath)) {
                prop.load(envStream);
                log.info(".env 文件加载成功，已覆盖 config.properties 中的同名属性");
            } catch (Exception e) {
                log.warn(".env 文件读取失败: {}", e.getMessage());
            }
        } else {
            log.info("未找到 .env 文件，跳过");
        }

        // 3. 环境变量 + JVM 参数覆盖（最高优先级）
        // 对 prop 中已存在的每个 key，检查是否有同名变量
        for (String key : prop.stringPropertyNames()) {
            // JVM 参数优先
            String value = System.getProperty(key);

            // 再查环境变量
            if (value == null || value.isBlank()) {
                String envKey = "APP_" + key
                        .replace(".", "_")
                        .toUpperCase();
                value = System.getenv(envKey);
            }
            if (value != null && !value.isBlank()) {
                prop.setProperty(key, value);
            }
        }

        return prop;
    }
}
