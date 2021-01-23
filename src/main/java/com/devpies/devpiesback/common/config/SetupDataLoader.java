package com.devpies.devpiesback.common.config;

import com.devpies.devpiesback.auth.application.domain.model.Privilege;
import com.devpies.devpiesback.auth.application.domain.model.Role;
import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.repository.PrivilegeRepository;
import com.devpies.devpiesback.auth.application.domain.repository.RoleRepository;
import com.devpies.devpiesback.auth.application.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    PrivilegeRepository privilegeRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound(Roles.ADMIN.name(), adminPrivileges);
        createRoleIfNotFound(Roles.REPRESENTATIVE.name(), Arrays.asList(readPrivilege));
        createRoleIfNotFound(Roles.DOCTOR.name(), Arrays.asList(readPrivilege));
        createRoleIfNotFound(Roles.USER.name(), Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName(Roles.ADMIN.name());
        User user = new User("muradm373@gmail.com", "muradm373@gmail.com","muradm373");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        alreadySetup = true;
    }


    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, List<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}
