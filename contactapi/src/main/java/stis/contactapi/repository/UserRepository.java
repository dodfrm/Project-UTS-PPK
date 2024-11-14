package stis.contactapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import stis.contactapi.entity.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);

    public List<User> findByName(@Param("name") String name);
}