package com.jose30a2.clientes.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jose30a2.clientes.dto.ClientDTO;
import com.jose30a2.clientes.entities.Client;
import com.jose30a2.clientes.repositories.ClientRepository;
import com.jose30a2.clientes.service.service.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;



@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(Pageable pageable){
		
		Page<Client> result = repository.findAll(pageable);
		return result.map(x -> new ClientDTO(x));
		
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(long id) {
		
		return new ClientDTO(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso no encontrado")));
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		
		Client entity = new Client();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
		
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			
			throw new ResourceNotFoundException("Recurso no encontrado");
		}
	}
	
	// Incluo propagation a pesar de nao precisar por nao ter tablas com foreign keys
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if(!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso no encontrado");
		}else {
			repository.deleteById(id);
		}
	}

	private void copyDtoToEntity(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		
	}
}
