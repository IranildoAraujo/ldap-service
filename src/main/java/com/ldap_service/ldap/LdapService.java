package com.ldap_service.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

@Service
public class LdapService {

    @Autowired
    private LdapTemplate ldapTemplate;

	public DirContextOperations searchUserByUsername(String username) {
		EqualsFilter filter = new EqualsFilter("uid", username);
		return ldapTemplate.searchForObject("ou=users", filter.encode(), (ctx) -> (DirContextOperations) ctx);
	}

    /*public void addUser(String dn, DirContextOperations context) {
        ldapTemplate.bind(dn, context);
    }*/

    public void updateUser(String dn, DirContextOperations context) {
        ldapTemplate.modifyAttributes(dn, context.getModificationItems());
    }

    public void deleteUser(String dn) {
        ldapTemplate.unbind(dn);
    }
}

