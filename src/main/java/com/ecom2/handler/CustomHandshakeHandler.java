package com.ecom2.handler;

import com.ecom2.auth.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

@Component
@Slf4j
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public CustomHandshakeHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        URI uri = request.getURI();
        MultiValueMap<String, String> queryParams =
                UriComponentsBuilder.fromUri(uri).build().getQueryParams();

        String token = queryParams.getFirst("token");

        if (token != null && jwtTokenProvider.validateJwtToken(token)) {
            String username = jwtTokenProvider.getUserNameFromJwt(token);
            log.info("✅ WebSocket xác thực thành công cho user: {}", username);
            return () -> username;
        }

        log.warn("❌ WebSocket xác thực thất bại - Token không hợp lệ: {}", token);
        return null;
    }
}
