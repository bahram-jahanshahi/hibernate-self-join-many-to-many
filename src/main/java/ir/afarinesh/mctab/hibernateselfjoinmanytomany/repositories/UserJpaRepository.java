package ir.afarinesh.mctab.hibernateselfjoinmanytomany.repositories;

import ir.afarinesh.mctab.hibernateselfjoinmanytomany.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
