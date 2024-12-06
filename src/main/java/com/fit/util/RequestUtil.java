package com.fit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @AUTO 请求对象工具类
 * @Author AIM
 * @DATE 2018/6/15
 */
public class RequestUtil {

    private static Logger log = LoggerFactory.getLogger(RequestUtil.class);

    private RequestUtil() {
        throw new Error("工具类不能实例化！");
    }

    public static boolean isEmpty(CharSequence string) {
        return string == null || string.length() == 0;
    }

    public static boolean containsOnlyWhitespaces(CharSequence string) {
        int size = string.length();

        for (int i = 0; i < size; ++i) {
            char c = string.charAt(i);
            if (!(c <= ' ')) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNullOrBlank(CharSequence str) {
        return str == null || containsOnlyWhitespaces(str);
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return "";
        } else if (array.length == 1) {
            return String.valueOf(array[0]);
        } else {
            StringBuilder sb = new StringBuilder(array.length * 16);

            for (int i = 0; i < array.length; ++i) {
                if (i > 0) {
                    sb.append(separator);
                }

                sb.append(array[i]);
            }

            return sb.toString();
        }
    }

    public static String getsimpleQueryParam(HttpServletRequest request, String name) {
        if (isNullOrBlank(name)) {
            return null;
        } else if (request.getMethod().equalsIgnoreCase("POST")) {
            return request.getParameter(name);
        } else {
            String s = request.getQueryString();
            if (isNullOrBlank(s)) {
                return null;
            } else {
                try {
                    s = URLDecoder.decode(s, "UTF-8");
                } catch (UnsupportedEncodingException var4) {
                    log.error("encoding UTF-8 not support?", var4);
                }

                String[] values = (String[]) parseQueryString(s).get(name);
                return values != null && values.length > 0 ? values[values.length - 1] : null;
            }
        }
    }

    public static Map<String, Object> getSimpleQueryParams(HttpServletRequest request) {
        Map map;
        if (request.getMethod().equalsIgnoreCase("POST")) {
            map = request.getParameterMap();
        } else {
            String s = request.getQueryString();
            if (isNullOrBlank(s)) {
                return new HashMap();
            }

            try {
                s = URLDecoder.decode(s, "UTF-8");
            } catch (UnsupportedEncodingException var6) {
                log.error("encoding UTF-8 not support?", var6);
            }

            map = parseQueryString(s);
        }

        Map<String, Object> params = new HashMap(map.size());
        Iterator var5 = map.entrySet().iterator();

        while (var5.hasNext()) {
            Map.Entry<String, String[]> entry = (Map.Entry) var5.next();
            int len = ((String[]) entry.getValue()).length;
            if (len == 1) {
                params.put((String) entry.getKey(), ((String[]) entry.getValue())[0]);
            } else if (len > 1) {
                params.put((String) entry.getKey(), entry.getValue());
            }
        }

        return params;
    }

    public static Map<String, String[]> parseQueryString(String s) {
        String[] valArray = null;
        if (s == null) {
            throw new IllegalArgumentException();
        } else {
            Map<String, String[]> ht = new HashMap();
            StringTokenizer st = new StringTokenizer(s, "&");

            while (true) {
                String pair;
                int pos;
                do {
                    if (!st.hasMoreTokens()) {
                        return ht;
                    }

                    pair = st.nextToken();
                    pos = pair.indexOf(61);
                } while (pos == -1);

                String key = pair.substring(0, pos);
                String val = pair.substring(pos + 1, pair.length());
                if (!ht.containsKey(key)) {
                    valArray = new String[]{val};
                } else {
                    String[] oldVals = (String[]) ht.get(key);
                    valArray = new String[oldVals.length + 1];

                    for (int i = 0; i < oldVals.length; ++i) {
                        valArray[i] = oldVals[i];
                    }

                    valArray[oldVals.length] = val;
                }

                ht.put(key, valArray);
            }
        }
    }

    public static Map<String, String> getRequestMap(HttpServletRequest request, String prefix) {
        return getRequestMap(request, prefix, false);
    }

    /**
     * 请求参数转Map对象有前缀
     *
     * @param request 客户端的请求对象
     * @param prefix  前缀
     */
    public static Map<String, String> getRequestMapWithPrefix(HttpServletRequest request, String prefix) {
        return getRequestMap(request, prefix, true);
    }

    /**
     * 请求参数中指定前缀转Map对象
     *
     * @param request        客户端的请求对象
     * @param prefix         前缀
     * @param nameWithPrefix 是否有key值前缀名
     */
    private static Map<String, String> getRequestMap(HttpServletRequest request, String prefix, boolean nameWithPrefix) {
        Map<String, String> map = new HashMap();
        Enumeration names = request.getParameterNames();

        while (names.hasMoreElements()) {//测试此枚举是否包含更多的元素
            String name = (String) names.nextElement();
            // 判断name开头是否与指定的字符串匹配
            if (name.startsWith(prefix)) {
                String key = nameWithPrefix ? name : name.substring(prefix.length());
                String value = join(request.getParameterValues(name), ",");
                map.put(key, value);
            }
        }

        return map;
    }

    public static String getLocation(HttpServletRequest request) {
        UrlPathHelper helper = new UrlPathHelper();
        StringBuffer buff = request.getRequestURL();
        String uri = request.getRequestURI();
        String origUri = helper.getOriginatingRequestUri(request);
        buff.replace(buff.length() - uri.length(), buff.length(), origUri);
        String queryString = helper.getOriginatingQueryString(request);
        if (queryString != null) {
            buff.append("?").append(queryString);
        }

        return buff.toString();
    }

    /**
     * 判断是否ajax请求
     *
     * @param request 客户端的请求对象
     */
    public static boolean isAjax(HttpServletRequest request) {
        return request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString());
    }

    /**
     * 返回输出
     *
     * @param str      返回信息
     * @param response 服务器的响应对象
     */
    public static void writeToBrowser(String str, HttpServletResponse response) {
        //response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            out.print(str);
            out.flush();
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }
}
