package com.ldap_service.person;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Attribute.Type;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

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
@Entry(objectClasses = {"person"})
public class Person {
	
	@Attribute(name = "entryUUID")
	private String entryUUID;
	
	@Id
    @JsonSerialize(using = LdapNameSerializer.class)
	@JsonDeserialize(using = NameDeserializer.class)
	private Name dn;

	@Attribute(name = "cn")
	private String cn;

	@Attribute(name = "sn")
	private String sn;

	@Attribute(name = "telephoneNumber")
	private Long telephoneNumber;
	
	@Attribute(name = "objectClass")
	private List<String> objectClasses;
	
	@Attribute(name = "description")
	private List<String> description;

	@Attribute(type = Type.BINARY)
	byte[] jpegPhoto;

	public Person() {
		super();
	}
	
	public PersonDTO toDTO() {
		return PersonDTO.builder()
				
				.entryUUID(entryUUID)

				.dn(dn)
				
				.cn(cn)
				
				.sn(sn)
				
				.telephoneNumber(telephoneNumber)
				
				.objectClasses(objectClasses)
				
				.description(description)
				
				.jpegPhoto(jpegPhoto).build();
	}
}