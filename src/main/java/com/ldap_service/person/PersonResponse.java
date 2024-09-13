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
public class PersonResponse {
	
	private String entryUUID;

	@JsonSerialize(using = LdapNameSerializer.class)
	@JsonDeserialize(using = NameDeserializer.class)
	private Name dn;
	
	private String ou;

	private String cn;
	
	private Long telephoneNumber;
	
	private String sn;

	private List<String> description;

	private List<String> objectClasses;

	byte[] jpegPhoto;

	public PersonResponse() {
		super();
	}
}