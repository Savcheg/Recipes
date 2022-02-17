package recipes.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import recipes.models.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUsername(String username);
}
