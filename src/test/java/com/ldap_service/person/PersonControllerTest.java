package com.ldap_service.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPException;

@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerTest {

	private InMemoryDirectoryServer server;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	void setUp() throws LDAPException {
		InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=example,dc=com");
		config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("default", 1389));
		config.setSchema(null);
		server = new InMemoryDirectoryServer(config);
		server.importFromLDIF(true, new File("src/test/resources/test-data.ldif").getAbsolutePath());
		server.startListening();
	}

	@AfterEach
	void tearDown() {
		server.shutDown(true);
	}

	//TODO: RETORNANDO VÁZIO, VERIFICAR O QUE HOUVE
	@Test
	void findAllShouldReturnAllPersons() {
	    List<PersonResponse> persons = this.restTemplate.exchange(
	        "http://localhost:" + port + "/api/v1/personas",
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<List<PersonResponse>>() {}
	    ).getBody();

	    System.out.println(">>>>>>>>>>>>>>>[PERSONS]: " + persons);

	    assertThat(persons).isNotNull();
	    assertThat(persons.size()).isEqualTo(3);
	}
}