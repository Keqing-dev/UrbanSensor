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

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user =
                usersRepository.findFirstByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Datos " +
                        "Inválidos, Intenta Nuevamente"));

        return new User(user.getId(), user.getPassword(), new ArrayList<>());
    }
}
