package org.example.chatserice2.service;

import org.example.chatserice2.entity.ERole;
import org.example.chatserice2.entity.RoleEntity;
import org.example.chatserice2.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<RoleEntity> findByName(ERole name) {
        return roleRepository.findByName(name);
    }

    public RoleEntity saveRole(RoleEntity role) {
        return roleRepository.save(role);
    }
}
