package com.example.week2.service;

import com.example.week2.dto.LoginRequestDto;
import com.example.week2.dto.ResponseDto;
import com.example.week2.dto.SignupRequestDto;
import com.example.week2.dto.StatusEnum;
import com.example.week2.entity.User;
import com.example.week2.entity.UserRoleEnum;
import com.example.week2.exception.CustomException;
import com.example.week2.jwt.JwtUtil;
import com.example.week2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.week2.exception.ErrorCode.DUPLICATED_USERNAME;
import static com.example.week2.exception.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    @Transactional
    public ResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new CustomException(DUPLICATED_USERNAME);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);
        return new ResponseDto(StatusEnum.OK);                // DB에 정상적으로 저장 되었을 경우 결과 리턴
    }

    @Transactional(readOnly = true)
    public ResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER)
        );
        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(NOT_FOUND_USER);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return new ResponseDto(StatusEnum.OK);
    }
}