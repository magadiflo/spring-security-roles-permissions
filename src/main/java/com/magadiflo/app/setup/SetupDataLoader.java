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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * El ContextRefreshEvent se genera cuando se inicializa o actualiza un contexto de aplicación,
 * lo que significa que el método onApplicationEvent(...) se puede ejecutar más de una vez. Por lo tanto,
 * es posible que deba poner un tipo de estado en su componente para asegurarse de que el código
 * de inicialización se ejecute solo una vez. En nuestro caso usamos una bandera "alreadySetup"
 */
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetupDataLoader.class);
    private boolean alreadySetup = false;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(IUserRepository userRepository, IRoleRepository roleRepository,
                           IPermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.info("Accediendo al método onApplicationEvent(...)");
        LOG.info("Número de beans inicializados en el contenedor: {}", event.getApplicationContext().getBeanDefinitionCount());

        if (!this.alreadySetup) {
            // Creamos los permisos iniciales
            Permission readStudentPermission = this.createPermissionIfNotFound("student:read");
            Permission writeStudentPermission = this.createPermissionIfNotFound("student:write");
            Permission readCoursePermission = this.createPermissionIfNotFound("course:read");
            Permission writeCoursePermission = this.createPermissionIfNotFound("course:write");

            //Creamos los roles iniciales
            List<Permission> adminPermissions = new ArrayList<>(Arrays.asList(readStudentPermission, writeStudentPermission, readCoursePermission, writeCoursePermission));
            List<Permission> userPermissions = new ArrayList<>(Arrays.asList(readStudentPermission, readCoursePermission));

            Role adminRole = this.createRoleIfNotFound("ROLE_ADMIN", adminPermissions);
            Role userRole = this.createRoleIfNotFound("ROLE_USER", userPermissions);

            //Creamos un usuario inicial
            this.createUserIfNotFound("Test", "Test", "test@test.com", "test", new ArrayList<>(Arrays.asList(adminRole)));

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
            user.setPassword(this.passwordEncoder.encode(password));
            user.setEnabled(true);
        }
        user.setRoles(roles);
        //Si el id del user tiene valor se hará un update, caso contrario se creará nuevo user
        return this.userRepository.save(user);
    }

}
/**
 * 3. Configurar permisos (privilegios) y roles
 * A continuación, centrémonos en hacer una configuración temprana de los privilegios y roles en el sistema.
 * Vincularemos esto al inicio de la aplicación y usaremos un ApplicationListener en ContextRefreshedEvent
 * para cargar nuestros datos iniciales en el inicio del servidor.
 * <p>
 * Entonces, ¿qué está sucediendo durante este simple código de configuración? Nada complicado:
 * <p>
 * - Estamos creando los permisos (privilegios).
 * - Luego estamos creando los roles y asignándoles los permisos.
 * - Finalmente, estamos creando un usuario y asignándole un rol.
 * <p>
 * Observe cómo estamos usando una bandera (indicador) alreadySetup para determinar si la instalación (generación de
 * permisos, roles y usuario) debe ejecutarse o no.
 * <p>
 * Esto se debe simplemente a que ContextRefreshedEvent se puede activar varias veces dependiendo de cuántos contextos
 * hayamos configurado en nuestra aplicación. Y solo queremos ejecutar la configuración una vez.
 * <p>
 * Dos notas rápidas aquí.
 * - Primero veremos la terminología.
 * Estamos usando los términos Permiso (Privilegio) – Rol aquí. Pero en Spring, estos son ligeramente diferentes.
 * En Spring, nuestro permiso (privilegio) se conoce como Rol y también como una autoridad (authority) (otorgada),
 * lo cual es un poco confuso.
 * Esto no es un problema para la implementación, por supuesto, pero definitivamente vale la pena señalarlo.
 * <p>
 * - En segundo lugar, estos roles de Spring (nuestros permisos (privilegios)) necesitan un prefijo.
 * De forma predeterminada, ese prefijo es "ROLE", pero se puede cambiar. No estamos usando ese prefijo aquí,
 * solo para mantener las cosas simples, pero tenga en cuenta que será necesario si no lo estamos cambiando explícitamente.
 */