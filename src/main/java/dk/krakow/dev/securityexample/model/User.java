package dk.krakow.dev.securityexample.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "system_users")
@ToString()
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Builder.Default
	@Column(name="enabled")
	private boolean enabled = true;

	@NotNull
	@Column(name="username", unique = true, nullable = false)
	private String username;

	@NotNull
	@Column(name="password")
	private String password;

	@ManyToMany
	@JoinTable( name="user_roles", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
	Set<Role> roles;
}
