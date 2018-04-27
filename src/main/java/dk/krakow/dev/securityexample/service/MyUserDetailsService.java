package dk.krakow.dev.securityexample.service;

import dk.krakow.dev.securityexample.model.MyUserPrincipal;
import dk.krakow.dev.securityexample.model.User;
import dk.krakow.dev.securityexample.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public MyUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(s);
		if (user==null) throw new UsernameNotFoundException(s);
		return new MyUserPrincipal(user);
	}
}
