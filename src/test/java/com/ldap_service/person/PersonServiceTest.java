package com.ldap_service.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PersonServiceTest {

	private PersonService personService;

	@MockBean
	private LdapTemplate ldapTemplate;

	private Name dn;

	@BeforeEach
	public void setup() {
		ldapTemplate = mock(LdapTemplate.class);
		personService = new PersonService(ldapTemplate);
	}

	@Test
	@DisplayName("Buscar pessoa por Uid")
	void test1() {
		DirContextOperations mockDirContextOperations = mock(DirContextOperations.class);
		when(mockDirContextOperations.getStringAttribute("cn")).thenReturn("Some Person");
		when(ldapTemplate.findOne(any(ContainerCriteria.class), eq(Person.class))).thenReturn(getPerson());

		var person = personService.findByUuid("some.person");
		assertEquals(getPerson(), person);
	}
	
	@Test
	@DisplayName("Não deve Buscar pessoa por Uid")
	void test2() {
		DirContextOperations mockDirContextOperations = mock(DirContextOperations.class);
		when(mockDirContextOperations.getStringAttribute("cn")).thenReturn(null);
		when(ldapTemplate.findOne(any(ContainerCriteria.class), eq(Person.class))).thenReturn(null);

		var person = personService.findByUuid("x");
		System.out.println("[PERSON ATUAL]: " + person);
		assertNotEquals(getPerson(), person);
	}

	public Person getPerson() {
		try {
			List<String> objectClasses = List.of("top", "person", "organizationalPerson", "inetOrgPerson");
			dn = new LdapName("dc=jayway,dc=se");
			dn.add("c=Sweden");
			dn.add("ou=company1");
			dn.add("cn=Some Person");

			String cn = "Some Person";
			String sn = "Person";
			List<String> description = List.of("Sweden, Company1, Some Person");
			Long telephoneNumber = 46555123456L;
			byte[] jpegPhotoMock = new byte[] { 0x01, 0x02, 0x03 };

			var person = Person.builder().objectClasses(objectClasses).cn(cn).dn(dn).sn(sn).description(description)
					.telephoneNumber(telephoneNumber).jpegPhoto(jpegPhotoMock).build();
			return person;
		} catch (InvalidNameException e) {
			return null;
		}
	}

}
