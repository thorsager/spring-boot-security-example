package dk.krakow.dev.securityexample.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "privileges")
@ToString(exclude = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Privilege {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;

	@Column(name="name")
	private String name;

	@JsonIgnore
	@ManyToMany( mappedBy = "privileges")
	Set<Role> roles;
}
