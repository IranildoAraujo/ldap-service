package com.ldap_service.ldap;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;

@Component
public class LdapUserRepository {

    private final LdapTemplate ldapTemplate;

    public LdapUserRepository(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public List<DirContextOperations> buscarUsuarios(String filtro) {
        EqualsFilter filter = new EqualsFilter("uid", filtro);
        return ldapTemplate.search(
                "",
                filter.encode(),
                new AttributesMapper<>() {
                    @Override
                    public DirContextOperations mapFromAttributes(Attributes attrs) throws NamingException {
                        return (DirContextOperations) attrs;
                    }
                }
        );
    }
    // ... outros métodos que usam LdapTemplate ...
}