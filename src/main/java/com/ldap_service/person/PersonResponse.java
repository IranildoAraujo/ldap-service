package com.ldap_service.person;

import java.util.List;

import javax.naming.Name;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PersonResponse {
	
	private String entryUUID;

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