package inciDashboard_e5a.services;

import inciDashboard_e5a.model.User;
import inciDashboard_e5a.repositories.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
	User user = usersRepository.findByIdentificador(dni);

	Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
	grantedAuthorities.add(new SimpleGrantedAuthority("PUBLIC"));

	return new org.springframework.security.core.userdetails.User(user.getIdentificador(), user.getPassword(),
		grantedAuthorities);
    }
}
