package dk.krakow.dev.securityexample.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "auth_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "user")
public class AuthToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@NotNull
	@Column(name = "token", unique = true, nullable = false)
	private String token;


	@Builder.Default
	@Column(name = "enabled", nullable = false)
	private boolean enabled = true;

	@Column(name = "description")
	private String description;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;
}
