package com.nyoongoon.fullstackjava.service;


import com.nyoongoon.fullstackjava.domain.User;
import com.nyoongoon.fullstackjava.exception.InvalidSiginInformation;
import com.nyoongoon.fullstackjava.repository.UserRepository;
import com.nyoongoon.fullstackjava.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    public void sigin(Login login){
          //DB 조회
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSiginInformation::new);
        user.addSession();
    }
}
