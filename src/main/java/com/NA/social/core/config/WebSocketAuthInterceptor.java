package com.NA.social.core.config;

import com.NA.social.core.entity.User;
import com.NA.social.core.repository.UserRepository;
import com.NA.social.core.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = servletRequest.getServletRequest().getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return false;
            }
            token = token.substring(7);

            String username = jwtService.extractUserName(token);
            if (username == null || username.isEmpty()) {
                return false;
            }

            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return false;
            }

            User user = userOptional.get();
            if (!jwtService.isTokenValid(token, user)) {
                return false;
            }

            // Lưu username vào attributes để sử dụng trong WebSocket
            attributes.put("user", username);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
