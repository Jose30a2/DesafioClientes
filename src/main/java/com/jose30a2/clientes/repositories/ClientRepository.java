package com.jose30a2.clientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jose30a2.clientes.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
