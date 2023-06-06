package lv.vladislavs.ewallet.repository;

import lv.vladislavs.ewallet.model.database.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPasswordHash(String email, String passwordHash);
}