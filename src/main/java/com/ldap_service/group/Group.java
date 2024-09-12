package com.ldap_service.group;

import java.util.Set;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entry(objectClasses = { "top", "groupOfUniqueNames" }, base = "cn=groups")
public class Group {

	@Attribute(name = "entryUUID")
	private String entryUUID;

	@Id
	private Name dn;

	@Attribute(name = "cn")
	@DnAttribute("cn")
	private String name;

	@Attribute(name = "uniqueMember")
	private Set<Name> members;

	public void addMember(Name member) {
		members.add(member);
	}

	public void removeMember(Name member) {
		members.remove(member);
	}
}