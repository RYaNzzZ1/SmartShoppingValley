package com.smartshoppingvalley.tools;

import javax.servlet.http.HttpServletRequest;

//@Component
public class GetToken {
    public static String getTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public static String getIdFromToken(String token) {
        return JwtTokenProvider.getIdFromToken(token);
    }
}
