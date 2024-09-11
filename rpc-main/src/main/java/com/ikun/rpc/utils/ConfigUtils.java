package com.ikun.rpc.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.yaml.YamlUtil;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * 配置工具类
 */
public class ConfigUtils {

    private static final String[] CONFIG_EXTENSIONS = {"properties", "yaml", "yml", "json"};
    private static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;

    /**
     * 加载配置对象
     *
     * @param tClass
     * @param prefix
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置对象，支持区分环境
     *
     * @param tClass
     * @param prefix
     * @param environment
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }

        T config = null;
        for (String extension : CONFIG_EXTENSIONS) {
            String configFileName = configFileBuilder + "." + extension;
            if (FileUtil.exist(configFileName)) {
                config = loadConfigByExtension(tClass, prefix, configFileName, extension);
                if (config != null) {
                    return config;
                }
            }
        }

        if (config == null) {
            throw new RuntimeException("读取不到配置文件");
        }
        return config;
    }

    /**
     * 根据配置文件类型加载配置对象
     *
     * @param tClass
     * @param prefix
     * @param configFileName
     * @param extension
     * @param <T>
     * @return
     */
    private static <T> T loadConfigByExtension(Class<T> tClass, String prefix, String configFileName, String extension) {
        try {
            if (StrUtil.equalsAnyIgnoreCase(extension, "yaml", "yml")) {
                Dict dict = YamlUtil.loadByPath(configFileName);
                Dict subDict = getSubDictByPrefix(dict, prefix);
                return subDict.toBean(tClass);
            } else if (StrUtil.equalsIgnoreCase(extension, "json")) {
                String jsonContent = FileUtil.readString(configFileName, DEFAULT_CHARSET);
                Object subJsonMap = getSubJsonByPrefix(JSONUtil.parseObj(jsonContent), prefix);
                return JSONUtil.toBean(JSONUtil.parseObj(subJsonMap), tClass);
            } else if (StrUtil.equalsIgnoreCase(extension, "properties")) {
                Props props = new Props(configFileName, DEFAULT_CHARSET);
                props.autoLoad(true);
                return props.toBean(tClass, prefix);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加载配置文件失败: " + configFileName, e);
        }
        return null;
    }

    // YAML 的多层前缀处理方法
    private static Dict getSubDictByPrefix(Dict dict, String prefix) {
        if (StrUtil.isBlank(prefix)) {
            return dict;
        }
        String[] keys = prefix.split("\\.");
        Dict subDict = dict;
        for (String key : keys) {
            subDict = Dict.parse(subDict.get(key));
            if (subDict == null) {
                throw new RuntimeException("找不到对应的前缀: " + prefix);
            }
        }
        return subDict;
    }

    // JSON 的多层前缀处理方法
    private static Object getSubJsonByPrefix(Object jsonMap, String prefix) {
        if (StrUtil.isBlank(prefix)) {
            return jsonMap;
        }
        String[] keys = prefix.split("\\.");
        Object subJson = jsonMap;
        for (String key : keys) {
            if (subJson instanceof Map) {
                subJson = ((Map<?, ?>) subJson).get(key);
            }
            if (subJson == null) {
                throw new RuntimeException("找不到对应的前缀: " + prefix);
            }
        }
        return subJson;
    }
}
