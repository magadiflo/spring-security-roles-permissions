package com.magadiflo.app.auth;

import com.magadiflo.app.domain.Permission;
import com.magadiflo.app.domain.Role;
import com.magadiflo.app.domain.User;
import com.magadiflo.app.repository.IUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ApplicationUserService implements UserDetailsService {

    private final IUserRepository userRepository;

    public ApplicationUserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("El usuario %s no fue encontrado en la BD", username));
        } else {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.isEnabled(),
                    true, true, true,
                    this.getAuthorities(user.getRoles()));
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return this.getGrantedAuthorities(this.getPermissions(roles));
    }

    /**
     * Método que devuelve una lista con el nombre de los roles y permisos.
     * Sí, todos estarán en una sola lista y serán considerados como authorities (por el lado de spring).
     * Ejemplo de la lista que podría retorna:
     * "ROLE_ADMIN", "student:read", "student:write", "course:read", "course:write"
     */
    private List<String> getPermissions(Collection<Role> roles) {
        return Stream.concat(this.getRolesNameAsPermission(roles).stream(), this.getNameOfEachPermission(roles).stream())
                .collect(Collectors.toList());
    }

    // Método que devuelve el nombre de cada rol dentro de una lista
    private List<String> getRolesNameAsPermission(Collection<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }

    /**
     * Método que devuelve a partir de cada uno de los roles el nombre de cada uno de sus permisos.
     * El flatMap recibe las colecciones de los permisos y como salida los aplana en un único stream de permisos.
     * El método por referencia: Collection::stream es igual a lambda: permissions -> permissions.stream()
     * Nota: Podría suceder que varios roles tengan el mismo permission, produciéndose duplicado en el retorno
     * de esta lista.
     * Solución: Se puede usar el operador distinct(), o usar un Set<> por ejemplo, en el método getGrantedAuthorities(...)
     * en vez de retornar un List<GrantedAuthority>, retornar un Set<GrantedAuthority>
     */
    //TODO: Hacer prueba de que un usuario puede tener varios roles y esos roles tener permisos que se repitan
    private List<String> getNameOfEachPermission(Collection<Role> roles) {
        return roles.stream()
                .map(Role::getPermissions)
                .flatMap(Collection::stream)
                .map(Permission::getName)
                //.distinct()
                .collect(Collectors.toList());
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
