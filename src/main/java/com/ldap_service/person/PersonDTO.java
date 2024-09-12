package com.ldap_service.person;

import java.util.List;
import java.util.Objects;

import javax.naming.Name;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PersonDTO {

	private String entryUUID;
	
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
		return Objects.nonNull(dn)
				? PersonResponse.builder().entryUUID(entryUUID).dn(dn).ou(ou).cn(cn).sn(sn)
						.telephoneNumber(telephoneNumber).objectClasses(objectClasses).description(description)
						.jpegPhoto(jpegPhoto).build()
				: null;

	}
}