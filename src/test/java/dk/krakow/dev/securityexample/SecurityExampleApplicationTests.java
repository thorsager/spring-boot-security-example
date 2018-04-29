package dk.krakow.dev.securityexample;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SecurityExampleApplicationTests {

	@Test
	public void contextLoads() {
	}


	@Test
	public void random_1() {
		String actual = SecurityExampleApplication.randomStr(10, 97, 122);
		assertTrue(actual.matches("[a-z]{10}"));
		System.out.println(actual);
	}

	@Test
	public void random_2() {
		String actual = SecurityExampleApplication.randomStr(4, 48, 57);
		assertTrue(actual.matches("[0-9]{4}"));
		System.out.println(actual);
	}

}
