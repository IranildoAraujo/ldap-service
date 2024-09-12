package com.ldap_service.group;

import java.util.List;
import java.util.stream.Stream;

import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapClient;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;

public class GroupRepo {
	@Autowired
	private LdapTemplate ldapTemplate;

	private LdapClient ldapClient;

	public void setLdapClient(LdapClient ldapClient) {
		this.ldapClient = ldapClient;
	}

	public String getAllGroupNames() {
		return ldapClient.search().query(LdapQueryBuilder.query().where("objectclass").is("group"))
				.toObject((Attributes attrs) -> attrs.get("cn").get().toString());
	}

	public Group create(Group group) {
		ldapTemplate.create(group);
		return group;
	}

	public Group findByUid(String uid) {
		return ldapTemplate.findOne(LdapQueryBuilder.query().where("uid").is(uid), Group.class);
	}

	public void update(Group group) {
		ldapTemplate.update(group);
	}

	public void delete(Group group) {
		ldapTemplate.delete(group);
	}

	public List<Group> findAll() {
		return ldapTemplate.findAll(Group.class);
	}

	public List<Group> findByLastName(String lastName) {
		return ldapTemplate.find(LdapQueryBuilder.query().where("sn").is(lastName), Group.class);
	}

	public Stream<Group> streamFindByLastName(String lastName) {
		return ldapTemplate.findForStream(LdapQueryBuilder.query().where("sn").is(lastName), Group.class);
	}
}
