package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Player;
import com.mycompany.myapp.repository.PlayerRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * REST controller for managing Player.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    @Inject
    private PlayerRepository playerRepository;

    /**
     * POST  /players : Create a new player.
     *
     * @param player the player to create
     * @return the ResponseEntity with status 201 (Created) and with body the new player, or with status 400 (Bad Request) if the player has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/players",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody Player player) throws URISyntaxException {
        log.debug("REST request to save Player : {}", player);
        if (player.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("player", "idexists", "A new player cannot already have an ID")).body(null);
        }
        Player result = playerRepository.save(player);
        return ResponseEntity.created(new URI("/api/players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("player", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /players : Updates an existing player.
     *
     * @param player the player to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated player,
     * or with status 400 (Bad Request) if the player is not valid,
     * or with status 500 (Internal Server Error) if the player couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/players",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Player> updatePlayer(@Valid @RequestBody Player player) throws URISyntaxException {
        log.debug("REST request to update Player : {}", player);
        if (player.getId() == null) {
            return createPlayer(player);
        }
        Player result = playerRepository.save(player);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("player", player.getId().toString()))
            .body(result);
    }

    /**
     * GET  /players : get all the players.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of players in body
     */
    @RequestMapping(value = "/players",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Player> getAllPlayers() {
        log.debug("REST request to get all Players");
        List<Player> players = playerRepository.findAll();
        return players;
    }

    /**
     * GET  /players/:id : get the "id" player.
     *
     * @param id the id of the player to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the player, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/players/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        log.debug("REST request to get Player : {}", id);
        Player player = playerRepository.findOne(id);
        return Optional.ofNullable(player)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /players/:id : delete the "id" player.
     *
     * @param id the id of the player to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/players/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        log.debug("REST request to delete Player : {}", id);
        playerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("player", id.toString())).build();
    }


    /** Pol y Vasil
     * GET  /apuestass -> get players by name.
     */
    @RequestMapping(value = "/players/byName/{name}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Player>> getPlayerByleagueName(@PathVariable String name){
        log.debug("REST request to get Jugador : {}", name);
        List<Player> player =playerRepository.findByNameEquals(name);

        return Optional.ofNullable(player)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /** Pol y Vasil
     * GET  /tp-by-canastas -> get all top players from a canstas filter.
     */



    @RequestMapping(value = "/bp-by-canastas/{canastasTotales}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Player>> get(@PathVariable int canastasTotales) {
        log.debug("REST request to get Jugador : {}", canastasTotales);
        return Optional.ofNullable(playerRepository.findAllByBasketsGreaterThanEqualOrderByBasketsDesc(canastasTotales))
            .map(jugador -> new ResponseEntity<>(
                jugador,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }




    // fecha > fecha usuario, fecha < fecha usuario , fecha > Between < fecha usuario


    /** Pol y Vasil
     * GET  /tp-by-date -> get all top players by date > filter.
     */



    @RequestMapping(value = "/fet-by-date/{fecha}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Player>> getgreater(@PathVariable String fecha) {
        String[] tokens = fecha.split("-");

        String data = tokens[0]+"."+tokens[1]+"."+tokens[2];

        DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedDate(
            FormatStyle.MEDIUM).withLocale(Locale.GERMAN);

        LocalDate xmas = LocalDate.parse(data, germanFormatter);

        log.debug("REST request to get Jugador : {}", fecha);
        return Optional.ofNullable(playerRepository.findAllByFechaNacimientoGreaterThanOrderByBasketsDesc(xmas))
            .map(jugador -> new ResponseEntity<>(
                jugador,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    /** Pol y Vasil
     * GET  /tp-by-date -> get all top players by date < filter.
     */



    @RequestMapping(value = "/fet-by-datel/{fecha}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Player>> getless(@PathVariable String fecha) {
        String[] tokens = fecha.split("-");

        String data = tokens[0]+"."+tokens[1]+"."+tokens[2];

        DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedDate(
            FormatStyle.MEDIUM).withLocale(Locale.GERMAN);

        LocalDate xmas = LocalDate.parse(data, germanFormatter);

        log.debug("REST request to get Jugador : {}", fecha);
        return Optional.ofNullable(playerRepository.findAllByFechaNacimientoLessThanOrderByBasketsDesc(xmas))
            .map(jugador -> new ResponseEntity<>(
                jugador,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    /** Pol y Vasil
     * GET  /tp-by-date -> get all top players by date < filter.
     */



    @RequestMapping(value = "/fet-by-datebw/{fecha}/{fecha2}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Player>> getbw(@PathVariable String fecha,@PathVariable String fecha2) {
        String[] tokens = fecha.split("-");

        String data = tokens[0]+"."+tokens[1]+"."+tokens[2];

        String[] tokens1 = fecha2.split("-");

        String data1 = tokens1[0]+"."+tokens1[1]+"."+tokens1[2];

        DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedDate(
            FormatStyle.MEDIUM).withLocale(Locale.GERMAN);

        LocalDate xmas = LocalDate.parse(data, germanFormatter);
        LocalDate xmas1 = LocalDate.parse(data1, germanFormatter);

        log.debug("REST request to get Jugador : {}", fecha);
        return Optional.ofNullable(playerRepository.findAllByFechaNacimientoBetweenOrderByBasketsDesc(xmas,xmas1))
            .map(jugador -> new ResponseEntity<>(
                jugador,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



}
