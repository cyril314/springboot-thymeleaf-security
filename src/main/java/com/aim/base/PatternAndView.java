package com.aim.base;

import com.aim.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @AUTO 页面构造模型
 * @FILE PatternAndView.java
 * @DATE 2017-9-14 下午10:05:40
 * @Author AIM
 */
public class PatternAndView extends ModelAndView {

    private Logger logger = LoggerFactory.getLogger(PatternAndView.class);

    public PatternAndView(String viewName) {
        super.setViewName(viewName);
    }

    public PatternAndView(String viewName, HttpServletRequest request, HttpServletResponse response) {
        String contextPath = request.getContextPath().equals("/") ? "" : request.getContextPath();
        String webPath = getURL(request);
        String port = ":" + ObjectUtil.toInt(Integer.valueOf(request.getServerPort()));
        if (generic_domain(request).equals("127.0.0.1")) {
        } else if (!generic_domain(request).equals("localhost")) {
            //webPath = "http://www." + generic_domain(request) + port + contextPath;
            webPath = webPath + port + contextPath;
        }
        super.setViewName(viewName);
        super.addObject("webpath", webPath);
    }

    /**
     * 字符串首位去空
     */
    protected String trimSpaces(String str) {
        while (str.startsWith(" ")) {
            str = str.substring(1, str.length()).trim();
        }
        while (str.endsWith(" ")) {
            str = str.substring(0, str.length() - 1).trim();
        }
        return str;
    }

    /**
     * 判断是不是IP地址
     */
    protected boolean isIp(String IP) {
        boolean b = false;
        IP = trimSpaces(IP);
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String[] s = IP.split("\\.");
            if ((Integer.parseInt(s[0]) < 255) && (Integer.parseInt(s[1]) < 255) && (Integer.parseInt(s[2]) < 255)
                    && (Integer.parseInt(s[3]) < 255))
                b = true;
        }
        return b;
    }

    /**
     * 获取请求的域名或IP
     */
    protected String generic_domain(HttpServletRequest request) {
        String system_domain = "localhost";
        String serverName = request.getServerName();
        if (isIp(serverName))
            system_domain = serverName;
        else {
            system_domain = serverName.substring(serverName.indexOf(".") + 1);
        }

        return system_domain;
    }

    /**
     * 请求地址
     */
    protected String getURL(HttpServletRequest request) {
        String contextPath = request.getContextPath().equals("/") ? "" : request.getContextPath();
        String url = "http://" + request.getServerName();
        if (ObjectUtil.toInt(Integer.valueOf(request.getServerPort())) != 80)
            url = url + ":" + ObjectUtil.toInt(Integer.valueOf(request.getServerPort())) + contextPath;
        else {
            url = url + contextPath;
        }
        // logger.info("请求地址为："+url);
        return url;
    }

    /**
     * URL解码
     */
    protected String decode(String s) {
        return decode(s, "UTF-8");
    }

    protected String decode(String s, String coding) {
        String ret = s;
        try {
            ret = URLDecoder.decode(s.trim(), coding);
        } catch (Exception localException) {
            logger.info("URL解码异常");
        }
        return ret;
    }

    /**
     * URL编码
     */
    protected String encode(String s) {
        return encode(s, "UTF-8");
    }

    protected String encode(String s, String coding) {
        String ret = s;
        try {
            ret = URLEncoder.encode(s.trim(), coding);
        } catch (Exception localException) {
            logger.info("URL编码异常");
        }
        return ret;
    }

    /**
     * 请求参数URL转MAP对象
     */
    protected Map<String, String> reqStr2Map(String s) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            String[] splitByte = s.split("&");
            for (String str : splitByte) {
                map.put(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=") + 1));
            }
        } catch (Exception e) {
            throw new RuntimeException("系统异常");
        }
        return map;
    }

    /**
     * 将MAP转换成URL
     */
    protected String reqMap2Str(Map<String, Object> map) {
        try {
            if (map == null) {
                return "";
            }
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sb.append(entry.getKey() + "=" + entry.getValue());
                sb.append("&");
            }
            if (sb.toString().endsWith("&")) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("系统异常");
        }
    }
}
