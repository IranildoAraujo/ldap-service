package com.ldap_service.person;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

//TODO: Era usado com esses métodos no PersonService:
/* (Mantido temporariamente para consultas)
    public List<PersonDTO> findAll() {
        LdapQueryBuilder query = LdapQueryBuilder.query()
                .where("objectclass").is("person"); // Filtro Explícito!
    	
        return ldapTemplate.search(LdapQueryBuilder.query()
                .where("objectclass").is("person"), new PersonAttributesMapper())
                .stream().map(Person::toDTO).toList();
    }
	
	public List<PersonDTO> findAll() {
	    return ldapTemplate.search(
	            LdapQueryBuilder.query()
	                    .where("objectclass").is("person")
	                    .and("cn").isPresent(), // Adicionado filtro para 'cn' presente 
	            new PersonAttributesMapper()
	    )
	            .stream().map(Person::toDTO).toList();
	}
	INFO: Bom para os testes de integração, mas problematico
	para funcionar em cenário de negócio, além de ter um código
	mais complexo.
*/
public class PersonAttributesMapper implements AttributesMapper<Person> {
	
    @Override
    public Person mapFromAttributes(Attributes attributes) throws NamingException {
        Person person = new Person();
        
        person.setCn(getSingleAttributeValue(attributes, "entryUUID"));
        person.setCn(getSingleAttributeValue(attributes, "dn"));
        person.setCn(getSingleAttributeValue(attributes, "cn"));
        person.setSn(getSingleAttributeValue(attributes, "sn"));
        person.setOu(getSingleAttributeValue(attributes, "ou"));
        person.setOu(getSingleAttributeValue(attributes, "jpegPhoto"));
        
        String telephoneNumberStr = getSingleAttributeValue(attributes, "telephoneNumber");
        if (telephoneNumberStr != null && !telephoneNumberStr.isEmpty()) {
            person.setTelephoneNumber(Long.parseLong(telephoneNumberStr));
        }
        
//        person.setTelephoneNumber(Long.parseLong(getAttributeValue(attributes, "telephoneNumber")));
        person.setDescription(getAttributeValues(attributes, "description"));
        person.setObjectClasses(getAttributeValues(attributes, "objectClass"));
        return person;
    }

    // Método auxiliar para obter o valor de um atributo como String
    private String getSingleAttributeValue(Attributes attributes, String attributeName) throws NamingException {
        List<String> values = getAttributeValues(attributes, attributeName);
        return !values.isEmpty() ? values.get(0) : null; 
    }
    
    private List<String> getAttributeValues(Attributes attributes, String attributeName) throws NamingException {
        List<String> values = new ArrayList<>();
        Attribute attribute = attributes.get(attributeName);
        if (attribute != null) {
            NamingEnumeration<?> attributeValues = attribute.getAll();
            while (attributeValues.hasMore()) {
                Object value = attributeValues.next();
                if (value != null) {
                    values.add(value.toString());
                }
            }
        }
        return values;
    }

}