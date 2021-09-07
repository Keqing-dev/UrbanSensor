package dev.keqing.urbansensor.service;

import dev.keqing.urbansensor.dao.UsersRepository;
import dev.keqing.urbansensor.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<Users> userAccountOptional = usersRepository.findById(username);
            if (userAccountOptional.isEmpty()) {
                throw new UsernameNotFoundException("Usuario no encontrado");
            }
            Users userAccount = userAccountOptional.get();
            return new User("mati2", "nose", new ArrayList<>());
        } catch (Exception e) {
            throw new UsernameNotFoundException("Nombre de usuario o contraseña incorrecta");
        }
    }
}
