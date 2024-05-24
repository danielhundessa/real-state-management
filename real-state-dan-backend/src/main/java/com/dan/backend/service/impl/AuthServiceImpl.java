package com.dan.backend.service.impl;

import com.dan.backend.enumSet.RoleType;
import com.dan.backend.repo.RoleRepo;
import com.dan.backend.repo.UserRepo;
import com.dan.backend.service.AuthService;
import com.dan.backend.util.JwtTokenUtil;
import com.dan.backend.domain.Role;
import com.dan.backend.domain.User;
import com.dan.backend.domain.dto.request.LoginRequest;
import com.dan.backend.domain.dto.request.RegisterRequest;
import com.dan.backend.domain.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final JwtTokenUtil jwtUtil;
    private final ModelMapper modelMapper;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {

            Authentication result = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            final UserDetails userDetails = userDetailsService.loadUserByUsername(result.getName());

            final String accessToken = jwtUtil.generateToken(userDetails);

//            final String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getEmail());
            var loginResponse = new LoginResponse(accessToken);
            return loginResponse;
        } catch (BadCredentialsException e) {
            System.out.println("ISSUE" + e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }


    }

    @Override
    public void register(RegisterRequest registerRequest) {
        try {
            //set role
            User user = modelMapper.map(registerRequest, User.class);

            RoleType roleValue = registerRequest.getIsOwner() ? RoleType.OWNER : RoleType.CUSTOMER;
            Role role = roleRepo.findByRole(roleValue);


            List<Role> roles = new ArrayList<>();
            roles.add(role);

            user.setRoles(roles);

            //set Password
            user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
            userRepo.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
