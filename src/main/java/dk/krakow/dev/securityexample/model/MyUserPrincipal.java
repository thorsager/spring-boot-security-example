package dk.krakow.dev.securityexample.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyUserPrincipal implements UserDetails {
	private User user;

	public MyUserPrincipal(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> granted = new HashSet<>();

		// Add all role names
		user.getRoles().stream()
				.map(r->new SimpleGrantedAuthority(r.getName()))
				.forEach(granted::add);

		// Add all privileges
		user.getRoles().stream()
				.flatMap(r->r.getPrivileges().stream())
				.map(p->new SimpleGrantedAuthority(p.getName()))
				.forEach(granted::add);
		return granted;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}
}
