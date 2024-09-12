package com.ldap_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {

    @Bean
    @Profile("!test")  // Apenas para ambientes que não são de teste
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://localhost:389");
        contextSource.setBase("dc=example,dc=com");
        contextSource.setUserDn("cn=admin,dc=example,dc=com");
        contextSource.setPassword("adminpassword");
        return contextSource;
    }

    @Bean
    @Profile("!test")  // Apenas para ambientes que não são de teste
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

}
