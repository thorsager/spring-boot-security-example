package dk.krakow.dev.securityexample.config;

import dk.krakow.dev.securityexample.service.ApiKeyAuthenticationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
@Order(1)
public class ApiKeySecurityConfig extends WebSecurityConfigurerAdapter {

	private final ApiKeyAuthenticationUserDetailsService apiKeyUserDetailsService;

	@Value("${app.principal_request_header}")
	private String principalRequestHeader;

	public ApiKeySecurityConfig(ApiKeyAuthenticationUserDetailsService apiKeyUserDetailsService) {
		this.apiKeyUserDetailsService = apiKeyUserDetailsService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.antMatcher("/api/**")
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterAt(headerAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);
	}


	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(keyAuthenticationProvider());
	}

	@Bean
	PreAuthenticatedAuthenticationProvider keyAuthenticationProvider() {
		PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
		authenticationProvider.setPreAuthenticatedUserDetailsService(apiKeyUserDetailsService);
		return authenticationProvider;
	}

	@Bean
	RequestHeaderAuthenticationFilter headerAuthenticationFilter() throws Exception {
		RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
		filter.setPrincipalRequestHeader(principalRequestHeader);
		filter.setAuthenticationManager(authenticationManager());
		filter.setExceptionIfHeaderMissing(false);
		return filter;
	}


}
