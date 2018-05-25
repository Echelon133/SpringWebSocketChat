package ml.echelon133.config;

import ml.echelon133.model.SpecialAuthority;
import ml.echelon133.service.ISpecialAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private ClientRegistrationRepository clientRegistrationRepository;
    private ISpecialAuthorityService authorityService;

    @Autowired
    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository, ISpecialAuthorityService authorityService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorityService = authorityService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                    .antMatchers("/", "/json/rooms", "/webjars/**", "/static/**").permitAll()
                    .antMatchers("/rooms/**").hasRole("USER")
                    .antMatchers("/json/special-authorities", "/admin", "/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated().and().oauth2Login().userInfoEndpoint().userService(oAuth2UserService());
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(this.clientRegistrationRepository);
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        return (userRequest) -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);
            // Github by default gives every user only ROLE_USER
            Collection<? extends GrantedAuthority> initialAuthorities = oAuth2User.getAuthorities();
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>(initialAuthorities);

            // take username from attributes, because oAuth2User.getName() does not contain actual github login
            String username = oAuth2User.getAttributes().get("login").toString();

            // Most of the users have only ROLE_USER, but if user has any additional authorities stored as SpecialAuthority
            // then add that authority to mappedAuthorities
            List<SpecialAuthority> specialAuthorities = authorityService.getAllByUsername(username);
            specialAuthorities.forEach(mappedAuthorities::add);

            // Extract fields needed to recreate OAuth2User with (possibly) updated authorities
            Map<String, Object> oAuth2UserAttributes = oAuth2User.getAttributes();
            String usernameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

            // replace oAuth2User with recreated user that has updated authorities if there were any stored as SpecialAuthority
            oAuth2User = new DefaultOAuth2User(mappedAuthorities, oAuth2UserAttributes, usernameAttributeName);
            return oAuth2User;
        };
    }
}
