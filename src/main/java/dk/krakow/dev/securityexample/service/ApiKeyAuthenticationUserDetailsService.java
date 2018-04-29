package dk.krakow.dev.securityexample.service;

import dk.krakow.dev.securityexample.model.AuthToken;
import dk.krakow.dev.securityexample.model.MyUserPrincipal;
import dk.krakow.dev.securityexample.repo.AuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	private final AuthTokenRepository authTokenRepository;

	@Autowired
	public ApiKeyAuthenticationUserDetailsService(AuthTokenRepository authTokenRepository) {
		this.authTokenRepository = authTokenRepository;
	}

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		String tokenValue = (String) token.getPrincipal();
		AuthToken at = authTokenRepository.findByTokenAndEnabled(tokenValue, true);
		if (at == null) throw new UsernameNotFoundException(tokenValue);
		return new MyUserPrincipal(at.getUser());
	}
}
