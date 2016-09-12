package com.github.abcmmee.wifiproxy;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class WifiProxyUtils {

    // 使构造器私有化
    private WifiProxyUtils() {

    }

    public static void setWifiProxy4(Context context, String host, int port) {
        WifiConfiguration configuration = getWifiConfiguration(context);

        try {
            // 获取WifiConfiguration中的linkProperties字段
            Field field = configuration.getClass().getField("linkProperties");
            Object linkProperties = field.get(configuration);

            // 获取LinkProperties类中setHttpProxy方法
            Class proxyPropertiesClass = Class.forName("android.net.ProxyProperties");
            Class linkPropertiesClass = Class.forName("android.net.LinkProperties");
            Method setHttpProxy = linkPropertiesClass.getDeclaredMethod("setHttpProxy", proxyPropertiesClass);
            setHttpProxy.setAccessible(true);

            // 使用ProxyProperties类的构造器，获得一个ProxyProperties对象
            Constructor constructor = proxyPropertiesClass.getConstructor(String.class, int.class, String.class);
            Object proxyProperties = constructor.newInstance(host, port, null);

            // WifiConfiguration.linkProperties.setHttpProxy(ProxyProperties proxyProperties)
            setHttpProxy.invoke(linkProperties, proxyProperties);

            // WifiConfiguration.proxySettings = WifiConfiguration.ProxySettings.STATIC;
            Field proxySettings = configuration.getClass().getField("proxySettings");
            proxySettings.set(configuration, Enum.valueOf((Class<Enum>) proxySettings.getType(), "STATIC"));
            reconnectWifi(context, configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setWifiProxy5(Context context, String host, int port) {
        WifiConfiguration configuration = getWifiConfiguration(context);

        try {
            // 获取WifiConfiguration类中的setProxy方法
            Class proxySettings = Class.forName("android.net.IpConfiguration$ProxySettings");
            Method setProxy = configuration.getClass().getDeclaredMethod("setProxy", proxySettings, ProxyInfo.class);
            setProxy.setAccessible(true);

            // 使用WifiConfiguration类中的setProxy方法
            ProxyInfo proxyInfo = ProxyInfo.buildDirectProxy(host, port);
            setProxy.invoke(configuration, Enum.valueOf(proxySettings, "STATIC"), proxyInfo);
            reconnectWifi(context, configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unsetWifiProxy4(Context context) {
        WifiConfiguration configuration = getWifiConfiguration(context);

        try {
            // 获取WifiConfiguration中的linkProperties字段
            Field field = configuration.getClass().getField("linkProperties");
            Object linkProperties = field.get(configuration);

            // 获取LinkProperties类中setHttpProxy方法
            Class proxyPropertiesClass = Class.forName("android.net.ProxyProperties");
            Class linkPropertiesClass = Class.forName("android.net.LinkProperties");
            Method setHttpProxy = linkPropertiesClass.getDeclaredMethod("setHttpProxy", proxyPropertiesClass);
            setHttpProxy.setAccessible(true);

            // 使用LinkProperties类中的setHttpProxy方法，传入null
            setHttpProxy.invoke(linkProperties, new Object[]{null});

            // WifiConfiguration.proxySettings = WifiConfiguration.ProxySettings.NONE;
            Field proxySettings = configuration.getClass().getField("proxySettings");
            proxySettings.set(configuration, Enum.valueOf((Class<Enum>) proxySettings.getType(), "NONE"));
            reconnectWifi(context, configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void unsetWifiProxy5(Context context) {
        WifiConfiguration configuration = getWifiConfiguration(context);

        try {
            // 获取WifiConfiguration类中的setProxy方法
            Class proxySettings = Class.forName("android.net.IpConfiguration$ProxySettings");
            Method setProxy = configuration.getClass().getDeclaredMethod("setProxy", proxySettings, ProxyInfo.class);
            setProxy.setAccessible(true);

            // 使用WifiConfiguration类中的setProxy方法
            setProxy.invoke(configuration, Enum.valueOf(proxySettings, "NONE"), null);
            reconnectWifi(context, configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取当前连接的wifi的WifiConfiguration对象
     *
     * @param context
     * @return
     */
    private static WifiConfiguration getWifiConfiguration(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        List<WifiConfiguration> configurationList = manager.getConfiguredNetworks();
        WifiConfiguration wifiConfig = null;
        int currentId = manager.getConnectionInfo().getNetworkId();
        for (WifiConfiguration configuration : configurationList) {
            if (configuration.networkId == currentId) {
                wifiConfig = configuration;
            }
        }
        return wifiConfig;
    }

    /**
     * 更新网络配置，重新连接wifi
     *
     * @param context
     * @param wifiConfiguration
     */
    private static void reconnectWifi(Context context, WifiConfiguration wifiConfiguration) {
        WifiManager manager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        manager.updateNetwork(wifiConfiguration);
        manager.disconnect();
        manager.reconnect();
    }
}
