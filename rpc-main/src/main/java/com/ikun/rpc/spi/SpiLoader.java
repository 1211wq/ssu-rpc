package com.ikun.rpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.ikun.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpiLoader {
    /*
     * 存储已加载的类
     * */
    private static final Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /*
     * 对象的实例缓存
     * */
    private static final Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /*
     * 系统spi目录
     * */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system";

    /*
     * 用户自定义spi目录
     * */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom";

    /*
     * 扫描路径
     * */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /*
     * 动态加载类列表
     * */
    private static final List<Class<?>> LOAD_CLASS_LIST = List.of(Serializer.class);

    /*
     * 加载所有类型
     * */
    public static void loadAll() {
        log.info("加载所有spi");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    /*
     * 获取某个接口的实例
     * */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader未加载 %s 类型", tClassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader的 %s 不存在key= %s 的类型", tClassName));
        }
        //  获取要加载的实现类型
        Class<?> implClass = keyClassMap.get(key);
        //  从实例缓存中加载指定类型的实例
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMsg = String.format("%s实例化失败", implClassName);
                throw new RuntimeException(e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }

    /*
     * 加载某个类型
     * */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("加载的类型为{}的spi", loadClass.getName());
        //  扫描路径，用于自定义的spi优先级高于系统的spi
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            String url = scanDir +"/" + loadClass.getName();
            List<URL> resources = ResourceUtil.getResources(url);
            //  读取每个资源文件
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strArray = line.split("=");
                        String key = strArray[0];
                        String className = strArray[1];
                        keyClassMap.put(key, Class.forName(className));
                    }
                } catch (Exception e) {
                    log.error("spi resource load error", e);
                }
            }
        }
        loaderMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }
}
