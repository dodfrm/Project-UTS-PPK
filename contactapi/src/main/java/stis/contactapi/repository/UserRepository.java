package stis.contactapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stis.contactapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}