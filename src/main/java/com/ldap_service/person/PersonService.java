package com.ldap_service.person;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

	private LdapTemplate ldapTemplate;

	public PersonService(LdapTemplate ldapTemplate) {
		super();
		this.ldapTemplate = ldapTemplate;
	}

	public Person create(PersonDTO personDTO) {
		var person = personDTO.toModel();
		var dnStr = person.getDn().toString();
		// Verifique se já existe uma entrada com o mesmo DN
		if (entryExists(dnStr)) {
			// Trate a existência da entrada (ex: lançando uma exceção ou retornando um
			// erro)
			throw new IllegalArgumentException("Já existe um usuário com o DN: " + dnStr);
		}

		System.out.println("DN: " + person.getDn());
		DirContextAdapter context = new DirContextAdapter(person.getDn());
		context.setAttributeValues("objectclass",
				new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
		context.setAttributeValue("cn", person.getCn());
		context.setAttributeValue("sn", person.getSn());
		context.setAttributeValue("userPassword", "{SHA}" + digestSHA("###123"));

		ldapTemplate.bind(context);

//		ldapTemplate.create(person);
		return person;
	}

	public void modify(String username, String password) {
		Name dn = LdapNameBuilder.newInstance().add("ou", "users").add("cn", username).build();
		DirContextOperations context = ldapTemplate.lookupContext(dn);

		context.setAttributeValues("objectclass",
				new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
		context.setAttributeValue("cn", username);
		context.setAttributeValue("sn", username);
		context.setAttributeValue("userPassword", digestSHA(password));

		ldapTemplate.modifyAttributes(context);
	}

	public void update(PersonDTO personDTO) {
		if (entryExists(personDTO.getDn().toString())) {
			throw new IllegalArgumentException("Não existe um usuário com o DN: " + personDTO.getEntryUUID());
		}
		ldapTemplate.update(personDTO.toModel());
	}

	public void delete(String uuid) {
		var person = findByUuid(uuid);
		ldapTemplate.delete(person);
	}

	// TODO: Mantido para consultas: 
	//	usa o new EqualsFilter("entryUUID", uuid) no filter
//	public void deletePersonByUuid(String uuid) {
//
//		LdapQuery query = LdapQueryBuilder.query().filter(new EqualsFilter("entryUUID", uuid));
//		List<Person> persons = ldapTemplate.find(query, Person.class);
//
//		if (persons.isEmpty()) {
//			throw new EmptyResultDataAccessException("DN não encontrado para o UUID: " + uuid, 1);
//		}
//
//		ldapTemplate.unbind(persons.get(0).getDn());
//	}
	
	public void deletePersonByUuid(String uuid) {
		var person = findByUuid(uuid);
		ldapTemplate.unbind(person.getDn());
	}
	
	public Person findByUuid(String uuid) {
		var ldapBuilder = LdapQueryBuilder.query().where("entryUUID").is(uuid);
		return ldapTemplate.findOne(ldapBuilder, Person.class);
	}

	//TODO: Funcional para cenários de negócio, ruim para testes
	// de integração, mantido por enquanto.
	public List<PersonDTO> findAll() {
		return ldapTemplate.findAll(Person.class).stream().map(Person::toDTO).toList();
	}
	
	public List<PersonDTO> findAllByLastName(String lastName) {
		return ldapTemplate.find(LdapQueryBuilder.query().where("sn").is(lastName), Person.class).stream()
				.map(Person::toDTO).toList();
	}

	private boolean entryExists(String dn) {
		return ldapTemplate.lookup(dn) != null;
	}

	// TODO: Método para gerar o hash SHA
	private String digestSHA(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1"); // Escolha o algoritmo SHA desejado
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			return no.toString(16); // Retorna o hash em hexadecimal
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
