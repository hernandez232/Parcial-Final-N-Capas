package com.uca.parcialfinalncapas.security;

import com.uca.parcialfinalncapas.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailService {
    private UserRepository userRepository;

}
