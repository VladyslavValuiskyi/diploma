package com.proarea.api.security.web;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class PathRequestMatcher implements RequestMatcher {

    private OrRequestMatcher skipMatcher;

    private RequestMatcher processingMatcher;

    public PathRequestMatcher(String processingPath, List<String> pathsToSkip) {
        if (pathsToSkip != null && !pathsToSkip.isEmpty()) {
            skipMatcher = new OrRequestMatcher(pathsToSkip.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList()));
        }
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return (skipMatcher == null || !skipMatcher.matches(request)) && processingMatcher.matches(request);
    }
}
