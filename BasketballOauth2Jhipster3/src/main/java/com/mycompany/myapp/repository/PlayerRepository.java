package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Player;

import org.springframework.data.jpa.repository.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the Player entity.
 */
public interface PlayerRepository extends JpaRepository<Player,Long> {

    List<Player> findByNameEquals(String name);
    List<Player> findAllByBasketsGreaterThanEqualOrderByBasketsDesc(Integer baskets);



    List<Player> findAllByFechaNacimientoGreaterThanOrderByBasketsDesc(LocalDate fechaNacimiento);
    List<Player> findAllByFechaNacimientoLessThanOrderByBasketsDesc(LocalDate fechaNacimiento);
    List<Player> findAllByFechaNacimientoBetweenOrderByBasketsDesc(LocalDate fechaNacimiento,LocalDate fecha);


}
