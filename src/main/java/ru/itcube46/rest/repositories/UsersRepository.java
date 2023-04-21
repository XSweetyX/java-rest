package ru.itcube46.rest.repositories;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ru.itcube46.rest.entities.User;

/**
 * Репозиторий - слой приложения, который отвечает за операции над данными.
 * Используется Spring Data JDBC.
 */
public interface UsersRepository extends CrudRepository<User, Long> {
    @Query("SELECT * FROM USERS WHERE EMAIL = :email")
    Optional<User> findByEmail(@Param("email") String email);
    
    @Modifying
    @Query("UPDATE USERS SET PASSWORD = :token WHERE EMAIL = :email")
    void updateToken(@Param("email") String email, @Param("token") String token);


    @Query("SELECT * FROM USERS WHERE ID = :id")
    Optional<User>  findByUserId(@Param("id") Long uId);

    @Modifying
    @Query("INSERT INTO users (email, password) VALUES (:email, :token)")
    void save(@Param("email") String email, @Param("token") String token);
}