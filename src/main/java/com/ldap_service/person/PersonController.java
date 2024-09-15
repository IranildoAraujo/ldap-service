package com.ldap_service.person;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/personas")
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping
	public List<PersonResponse> findAll() {
		return personService.findAll().stream().map(PersonDTO::toResponse).toList();
	}

	@GetMapping("{sn}")
	public List<PersonResponse> findAllByLastName(@PathVariable String sn) {
		return personService.findAllByLastName(sn).stream().map(PersonDTO::toResponse).toList();
	}

	@PostMapping
	public ResponseEntity<Person> create(@RequestBody PersonRequest personRequest) {
		return ResponseEntity.ok(personService.create(personRequest.toDTO()));
	}

	@PutMapping("/{username}/password")
	public ResponseEntity<String> updatePassword(@PathVariable String username, @RequestBody String password) {
		try {
			personService.updatePassword(username, password);
			return ResponseEntity.ok("Senha atualizada com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar a senha: " + e.getMessage());
		}
	}
	
	@PutMapping("/{uuid}")
	public ResponseEntity<String> updatePerson(
			@PathVariable String uuid, 
			@RequestBody PersonRequest personRequest) {
		try {
			personService.update(personRequest.toDTO());
			return ResponseEntity.ok("Senha atualizada com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar person: " + e.getMessage());
		}
	}

	@DeleteMapping("{uuid}")
	public void remove(@PathVariable String uuid) {
		personService.deletePersonByUuid(uuid);
	}

}
