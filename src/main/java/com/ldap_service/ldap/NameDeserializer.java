package com.ldap_service.ldap;

import java.io.IOException;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@JsonComponent
public class NameDeserializer extends JsonDeserializer<Name> {

    @Override
    public Name deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            return new LdapName(p.getText());
        } catch (InvalidNameException e) {
            throw new RuntimeException("Erro ao desserializar Name", e);
        }
    }
}
