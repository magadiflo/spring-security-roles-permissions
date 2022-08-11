package com.magadiflo.app.setup;

import com.magadiflo.app.domain.Permission;
import com.magadiflo.app.domain.Role;
import com.magadiflo.app.domain.User;
import com.magadiflo.app.repository.IPermissionRepository;
import com.magadiflo.app.repository.IRoleRepository;
import com.magadiflo.app.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetupDataLoader.class);

    private boolean alreadySetup = false;

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;

    // TODO: Inyectar el PasswordEncoder

    public SetupDataLoader(IUserRepository userRepository, IRoleRepository roleRepository, IPermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.info("Accediendo al método onApplicationEvent(...)");

        if (!this.alreadySetup) {

            //TODO: Creamos los permisos iniciales

            //TODO: Creamos los roles iniciales

            //TODO: Creamos un usuario inicial

            this.alreadySetup = true;
        }
    }

    @Transactional
    private Permission createPermissionIfNotFound(final String name) {
        Permission permission = this.permissionRepository.findByName(name);
        if (permission == null) {
            permission = new Permission(name);
            permission = this.permissionRepository.save(permission);
        }
        return permission;
    }

    @Transactional
    private Role createRoleIfNotFound(final String name, final Collection<Permission> permissions) {
        Role role = this.roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPermissions(permissions);
        //Si el id del role tiene valor se hará un update, caso contrario se creará nuevo role
        return this.roleRepository.save(role);
    }

    @Transactional
    private User createUserIfNotFound(final String firstName, final String lastName, final String email,
                                      final String password, final Collection<Role> roles) {
        User user = this.userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password); //TODO: Usar aquí el PasswordEncoder para encriptar password
            user.setEnabled(true);
        }
        user.setRoles(roles);
        //Si el id del user tiene valor se hará un update, caso contrario se creará nuevo user
        return this.userRepository.save(user);
    }

}
