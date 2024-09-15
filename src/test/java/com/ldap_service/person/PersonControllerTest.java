package com.ldap_service.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerTest {
	
	private static String PROPERTIES_LDAP_URL = "spring.ldap.urls";
	
	private static String VALUE_LDAP_URL = "ldap://localhost:";
	
	private static String BASE_DN = "dc=example,dc=com";
	
	private static String FILE_LDIF_PERSON = "src/test/resources/ldif/person.ldif";
	
	private static InMemoryDirectoryServer server;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
    @DynamicPropertySource
    static void registerLdapProperties(DynamicPropertyRegistry registry) throws LDAPException {
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(BASE_DN);
        // Não passar um número de porta em createLDAPConfig para que escolha seja aleatória
        config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("default"));
        config.setSchema(null);
        server = new InMemoryDirectoryServer(config);
        server.importFromLDIF(true, new File(FILE_LDIF_PERSON).getAbsolutePath());
        server.startListening();

        int ldapPort = server.getListenPort();
        registry.add(PROPERTIES_LDAP_URL, () -> VALUE_LDAP_URL + ldapPort);
    }

	@AfterEach
	void tearDown() {
		server.shutDown(true);
	}

	@Test
	@DisplayName("Deve listar todas as persons do arquivo ldif")
	void findAllShouldReturnAllPersons() {
		List<PersonResponse> persons = this.restTemplate.exchange("http://localhost:" + port + "/api/v1/personas",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<PersonResponse>>() {
				}).getBody();

		assertThat(persons).isNotNull();
		assertThat(persons.size()).isEqualTo(3);
	}
}