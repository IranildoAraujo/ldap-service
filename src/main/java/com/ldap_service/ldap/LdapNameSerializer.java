package com.ldap_service.ldap;
import java.io.IOException;

import javax.naming.ldap.LdapName;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class LdapNameSerializer extends JsonSerializer<LdapName> {

	@Override
	public void serialize(LdapName ldapName, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		if (ldapName != null) {
			jsonGenerator.writeString(ldapName.toString());
		} else {
			jsonGenerator.writeNull();
		}
	}
}
