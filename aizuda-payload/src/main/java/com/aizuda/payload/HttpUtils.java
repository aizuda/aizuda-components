//package com.aizuda.security;
//
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.http.HttpMethod;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
///**
// *
// * @ClassName: HttpUtils
// * @Description: http 工具类 获取请求中的参数
// * @author darren
// * @date 2019年8月7日
// *
// */
//public class HttpUtils {
//    /**
//     *
//     * @Title: getAllParams
//     * @Description: 将URL的参数和body参数合并
//     * @param @param request
//     * @param @return
//     * @param @throws IOException    参数
//     * @return SortedMap<String,String>    返回类型
//     * @throws
//     */
//    public static SortedMap<String, String> getAllParams(HttpServletRequest request) throws IOException {
//
//        SortedMap<String, String> result = new TreeMap<>();
//        //获取URL上的参数
//        Map<String, String> urlParams = getUrlParams(request);
//        for (Map.Entry entry : urlParams.entrySet()) {
//            result.put((String) entry.getKey(), (String) entry.getValue());
//        }
//        Map<String, String> allRequestParam = new HashMap<>(16);
//        // get请求不需要拿body参数
//        if (!HttpMethod.GET.name().equals(request.getMethod())) {
//            allRequestParam = getAllRequestParam(request);
//        }
//        //将URL的参数和body参数进行合并
//        if (allRequestParam != null) {
//            for (Map.Entry entry : allRequestParam.entrySet()) {
//                result.put((String) entry.getKey(), (String) entry.getValue());
//            }
//        }
//        return result;
//    }
//
//    /**
//     *
//     * @Title: getAllRequestParam
//     * @Description: 获取 Body 参数
//     * @param @param request
//     * @param @return
//     * @param @throws IOException    参数
//     * @return Map<String,String>    返回类型
//     * @throws
//     */
//    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) throws IOException {
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
//        String str = "";
//        StringBuilder wholeStr = new StringBuilder();
//        //一行一行的读取body体里面的内容；
//        while ((str = reader.readLine()) != null) {
//            wholeStr.append(str);
//        }
//        //转化成json对象
//        return JSONObject.parseObject(wholeStr.toString(), Map.class);
//    }
//
//    /**
//     *
//     * @Title: getUrlParams
//     * @Description: 将URL请求参数转换成Map
//     * @param @param request
//     * @param @return    参数
//     * @return Map<String,String>    返回类型
//     * @throws
//     */
//    public static Map<String, String> getUrlParams(HttpServletRequest request) {
//
//        String param = "";
//        try {
//            param = URLDecoder.decode(request.getQueryString(), "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Map<String, String> result = new HashMap<>(16);
//        String[] params = param.split("&");
//        for (String s : params) {
//            int index = s.indexOf("=");
//            result.put(s.substring(0, index), s.substring(index + 1));
//        }
//        return result;
//    }
//}