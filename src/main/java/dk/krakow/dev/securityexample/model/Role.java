package dk.krakow.dev.securityexample.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name="system_roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "users")
public class Role {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;

	@NotNull
	@Column(name="name", nullable = false, unique = true)
	private String name;

	@JsonIgnore
	@ManyToMany(mappedBy = "roles")
	private Set<User> users;

	@ManyToMany
	@JoinTable( name="roles_privileges", joinColumns = @JoinColumn(name="role_id"), inverseJoinColumns = @JoinColumn(name="privilege_id"))
	Set<Privilege> privileges;
}
