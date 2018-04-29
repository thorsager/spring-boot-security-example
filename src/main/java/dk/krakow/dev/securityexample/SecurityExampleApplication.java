package dk.krakow.dev.securityexample;

import dk.krakow.dev.securityexample.model.Privilege;
import dk.krakow.dev.securityexample.model.Role;
import dk.krakow.dev.securityexample.model.User;
import dk.krakow.dev.securityexample.repo.PrivilegeRepository;
import dk.krakow.dev.securityexample.repo.RoleRepository;
import dk.krakow.dev.securityexample.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityExampleApplication.class, args);
	}


	@Bean
	CommandLineRunner demo(PasswordEncoder passwordEncoder, UserRepository userRepository,
						   RoleRepository roleRepository, PrivilegeRepository privilegeRepository
	) {
		return args -> {

			privilegeRepository.saveAll(
					Arrays.asList(
							new Privilege(null,"PRIV_ALL",null),
							new Privilege(null,"PRIV_READ",null),
							new Privilege(null,"PRIV_WRITE",null)
					)
			);

			roleRepository.saveAll(
					Arrays.asList(
							Role.builder().name("ROLE_ADMIN")
									.privileges(setOf(privilegeRepository.findByName("PRIV_ALL")))
									.build(),
							Role.builder().name("ROLE_USER")
									.privileges(setOf(
											privilegeRepository.findByName("PRIV_WRITE"),
											privilegeRepository.findByName("PRIV_READ")
											))
									.build(),
							Role.builder().name("ROLE_GUEST")
									.privileges(setOf(privilegeRepository.findByName("PRIV_READ")))
									.build()
					)
			);

			String pwd = pin();


			userRepository.saveAll(
					Arrays.asList(
							User.builder()
									.username("john")
									.password(passwordEncoder.encode(pwd))
									.roles(setOf(roleRepository.findByName("ROLE_ADMIN")))
									.build(),
							User.builder()
									.username("joe")
									.password(passwordEncoder.encode(pwd))
									.roles(setOf(roleRepository.findByName("ROLE_USER")))
									.enabled(false)
									.build(),
							User.builder()
									.username("jane")
									.password(passwordEncoder.encode(pwd))
									.roles(setOf(roleRepository.findByName("ROLE_GUEST")))
									.build()
					)
			);
			User john = userRepository.findByUsername("john");
			john.addToken(token());
			john.addToken(token());

			User jane = userRepository.findByUsername("jane");
			jane.addToken(token());

			User joe = userRepository.findByUsername("joe");
			joe.addToken(token());
			joe.getTokens().forEach(t -> t.setEnabled(false));

			userRepository.saveAll(Arrays.asList(john, jane, joe));
			System.out.println();
			System.out.println("* Users created: " +
					userRepository.findAll()
							.stream()
							.map(u -> String.format("%s(enabled=%s)", u.getUsername(), u.isEnabled()))
							.collect(Collectors.joining(", "))
			);
			System.out.println("* Password for all users: " + pwd);


			System.out.println("* Tokens created: \n" +
					userRepository.findAll()
							.stream()
							.map(u ->
									String.format("* - %s: %s",
											u.getUsername(),
											u.getTokens().stream()
													.map(t -> String.format("%s(enabled=%s)", t.getToken(), t.isEnabled()))
													.collect(Collectors.joining(", "))
									)
							).collect(Collectors.joining("\n"))
			);
			System.out.println();

		};

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new Pbkdf2PasswordEncoder("d");
	}


	@SafeVarargs
	private final <T> Set<T> setOf(T... ts) {
		return new HashSet<>(Arrays.asList(ts));
	}

	@Controller
	public class HelloWorldController {
		@GetMapping("/")
		public String sayHello(Principal principal, Model model) {
			model.addAttribute("name",principal.getName());
			return "hello";
		}
	}

	@SuppressWarnings({"SpringElInspection", "ELValidationInJSP"})
	@RestController
	@RequestMapping("/rest")
	public class RestModelController {
		private UserRepository userRepository;
		private RoleRepository roleRepository;

		@Autowired
		RestModelController(UserRepository userRepository, RoleRepository roleRepository) {
			this.userRepository = userRepository;
			this.roleRepository = roleRepository;
		}


		// https://docs.spring.io/spring-security/site/docs/4.0.1.RELEASE/reference/htmlsingle/#el-common-built-in
		@GetMapping("/user")
		@PreAuthorize("hasAuthority('PRIV_ALL') or hasRole('ADMIN')")
		public Iterable<User> getAllUsers() {
			return userRepository.findAll();
		}

		@GetMapping("/role")
		public Iterable<Role> getAllRoles() {
			return roleRepository.findAll();
		}
	}

	@SuppressWarnings({"SpringElInspection", "ELValidationInJSP"})
	@RestController
	@RequestMapping("/api")
	public class ApiController {

		@GetMapping("/time")
		public String time() {
			return new Date().toString();
		}

		@GetMapping("/timeAsLong")
		@PreAuthorize("hasRole('ADMIN')")
		public Long timeAsLong() {
			return System.currentTimeMillis();
		}

	}

	private static String token() {
		return randomStr(10, 97, 122);
	}

	private static String pin() {
		return randomStr(4, 48, 57);
	}


	static String randomStr(int length, int origin, int bound) {
		return new Random()
				.ints(length, origin, bound)
				.mapToObj(Character::toChars)
				.map(String::new)
				.collect(Collectors.joining(""));
	}
}
