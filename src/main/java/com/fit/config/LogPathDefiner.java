package com.fit.config;

import ch.qos.logback.core.PropertyDefinerBase;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * @className: LogPathDefiner
 * @description: 日志路径
 * @author: Aim
 * @date: 2023/4/12
 **/
public class LogPathDefiner extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        String LogPath = jarF.getParentFile().toString() + File.separator + "logs" + File.separator;
        System.out.println(String.format(" - Log storage path: %s", LogPath));
        return LogPath;
    }
}