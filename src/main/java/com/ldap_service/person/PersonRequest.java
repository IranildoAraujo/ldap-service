package com.ldap_service.person;

import java.util.List;

import javax.naming.Name;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ldap_service.ldap.LdapNameSerializer;
import com.ldap_service.ldap.NameDeserializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PersonRequest {
	
	private String entryUUID;
	
	@JsonSerialize(using = LdapNameSerializer.class)
	@JsonDeserialize(using = NameDeserializer.class)
	private Name dn;

	private String cn;

	private String sn;
	
	private Long telephoneNumber;
	
	private List<String> objectClasses;

	private List<String> description;

	byte[] jpegPhoto;

	public PersonRequest() {
		super();
	}
	
	public PersonDTO toDTO() {

		var personBuilder = PersonDTO.builder();

		return personBuilder

				.entryUUID(entryUUID)

				.dn(dn)

				.cn(cn)

				.sn(sn)

				.telephoneNumber(telephoneNumber)

				.objectClasses(objectClasses)

				.description(description)

				.jpegPhoto(jpegPhoto)

				.build();
	}
}