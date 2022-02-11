package com.SprnigSecurity.jwtAuth.web.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseMap {
    public static Map<String, Object> creatResponseMap(int status, String message) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", status);
        responseMap.put("message", message);
        return responseMap;
    }
}
