package dev.keqing.urbansensor.utils;

import dev.keqing.urbansensor.dao.UserRepository;
import dev.keqing.urbansensor.entity.User;
import dev.keqing.urbansensor.entity.UserProject;
import dev.keqing.urbansensor.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Component
public class Validations {


    @Autowired
    private UserRepository userRepository;

    public User validateUser(HttpServletRequest request) throws CustomException {
        return userRepository.findById(request.getRemoteUser()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));
    }

    public void validateUserProject(User user, UserProject userProject) throws CustomException {
        if (!user.getId().equals(userProject.getUser().getId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "Usuario no autorizado.");
        }
    }

}
