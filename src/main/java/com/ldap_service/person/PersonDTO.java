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
public class PersonDTO {

	private String entryUUID;
	
	@JsonSerialize(using = LdapNameSerializer.class)
	@JsonDeserialize(using = NameDeserializer.class)
	private Name dn;
	
	private String ou;

	private String cn;

	private String sn;

	private Long telephoneNumber;
	
	private List<String> objectClasses;

	private List<String> description;

	byte[] jpegPhoto;

	public PersonDTO() {
		super();
	}

	public Person toModel() {
		return Person.builder().entryUUID(entryUUID).dn(dn).ou(ou).cn(cn).sn(sn).telephoneNumber(telephoneNumber)
				.objectClasses(objectClasses).description(description).jpegPhoto(jpegPhoto).build();
	}

	public PersonResponse toResponse() {
		return PersonResponse.builder().entryUUID(entryUUID).dn(dn != null ? dn : null).ou(ou).cn(cn).sn(sn)
				.telephoneNumber(telephoneNumber).objectClasses(objectClasses).description(description)
				.jpegPhoto(jpegPhoto).build();

	}
}