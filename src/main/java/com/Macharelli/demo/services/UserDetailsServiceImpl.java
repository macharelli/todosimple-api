package com.Macharelli.demo.services;

import com.Macharelli.demo.models.User;
import com.Macharelli.demo.repositories.UserRepository;
import com.Macharelli.demo.security.UserSpringSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = this.userRepository.findByUsername(username);
      if(Objects.isNull(user))
          throw new UsernameNotFoundException("Usuário não encontrados: "+ username);

       return new UserSpringSecurity(user.getId(), user.getUsername(),user.getPassword(),user.getProfiles());
    }
}
