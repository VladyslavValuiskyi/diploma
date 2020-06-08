package com.proarea.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proarea.api.repository.AuthoritiesRepository;
import com.proarea.api.repository.UserRepository;
import com.proarea.api.security.api.LoginController;
import com.proarea.api.security.jwt.JwtSettings;
import com.proarea.api.security.jwt.JwtTokenBuilder;
import com.proarea.api.security.jwt.TokenAuthenticationProvider;
import com.proarea.api.security.web.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/**";
    private static final String LOGIN_ENTRY_POINT = "/login";
    private static final String[] SWAGGER_RESOURCES = {"/v2/api-docs*", "/swagger-ui.html", "/webjars/springfox-swagger-ui/**", "/swagger-resources/**"};

    private final DataSource dataSource;

    private final UserRepository userRepository;

    private final AuthoritiesRepository authoritiesRepository;


    @Value("${application.api.version.prefix}")
    private String apiPrefix;

    @Autowired
    public SecurityConfig(DataSource dataSource, UserRepository userRepository, AuthoritiesRepository authoritiesRepository) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.trace("[SecurityConfig] [configure auth providers]");
        auth.authenticationProvider(tokenAuthenticationProvider());
        auth.jdbcAuthentication().dataSource(dataSource).authoritiesByUsernameQuery("select user_id, authority from authorities where user_id = (select user_id from users where username = ?)").passwordEncoder(passwordEncryptor());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(SWAGGER_RESOURCES);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.trace("[SecurityConfig] [configure]");
        http.csrf().disable().exceptionHandling().authenticationEntryPoint(authEntryPoint()).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/url/*").permitAll()
                .antMatchers(HttpMethod.GET, "/console/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user/registration").permitAll()
                .antMatchers(HttpMethod.POST, "/esp/register").permitAll()
                .antMatchers(HttpMethod.POST, "/user/password/restore").permitAll()
                .antMatchers(HttpMethod.POST, "/esp/data").permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(loginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(restTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private TokenAuthenticationFilter restTokenAuthenticationFilter() throws Exception {
        PathRequestMatcher matcher = new PathRequestMatcher(TOKEN_BASED_AUTH_ENTRY_POINT,
                Arrays.asList("/user/registration", "/esp/register", "/esp/data", "/user/password/restore", "/console/**"));
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(matcher, webAuthenticationUnsuccessfulHandler());
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    public TokenAuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider(jwtTokenBuilder(), userRepository);
    }

    @Bean
    public WebAuthenticationUnsuccessfulHandler webAuthenticationUnsuccessfulHandler() {
        return new WebAuthenticationUnsuccessfulHandler(objectMapper());
    }

    private LoginProcessingFilter loginProcessingFilter() throws Exception {
        LoginProcessingFilter filter = new LoginProcessingFilter(LOGIN_ENTRY_POINT, objectMapper(), jwtTokenBuilder(),
                webAuthenticationUnsuccessfulHandler(), userRepository, authoritiesRepository);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    public AuthenticationEntryPoint authEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public JwtTokenBuilder jwtTokenBuilder() {
        return new JwtTokenBuilder(jwtSettings());
    }

    @Bean
    @ConfigurationProperties(prefix = "application.security.jwt")
    public JwtSettings jwtSettings() {
        return new JwtSettings();
    }

    @Bean
    @ConfigurationProperties(prefix = "application.security.password")
    public PasswordMd5Encoder passwordEncryptor() {
        return new PasswordMd5Encoder();
    }

    @Bean
    public LoginController loginController() {
        return new LoginController();
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
