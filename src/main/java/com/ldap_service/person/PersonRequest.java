package com.ldap_service.person;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.support.LdapNameBuilder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PersonRequest {
	
	private String entryUUID;
	
	private String dn;
	
	private String ou;

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
		System.out.println(">>>>>> 1 - [TEST]: " + "cn=" + cn + ",ou=" + ou);
		System.out.println(">>>>>> 2 - [TEST]: " + dn);
		if (dn.equals("cn=" + cn + ",ou=" + ou)) {
			Name dn = LdapNameBuilder.newInstance().add("ou", ou).add("cn", cn).build();
			personBuilder.dn(dn);
		}

		return personBuilder

				.entryUUID(entryUUID)
				
				.ou(ou)

				.cn(cn)

				.sn(sn)

				.telephoneNumber(telephoneNumber)

				.objectClasses(objectClasses)

				.description(description)

				.jpegPhoto(jpegPhoto)

				.build();
	}
}