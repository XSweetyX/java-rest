package ru.itcube46.rest.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import ru.itcube46.rest.entities.User;
import ru.itcube46.rest.repositories.UsersRepository;


@Slf4j
@RestController
@RequestMapping(path = "api/login", produces = "application/json")
public class LoginController {
    UsersRepository usersRepository;
    

    public LoginController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping
    public String getToken(@RequestParam(value = "idToken") String idToken, @RequestParam(value = "accessToken") String accessToken) {
    JsonParser parser = new BasicJsonParser();

        // Construct the URL
        String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken;

        // Construct the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(idToken);

        // Construct the request entity
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Send the request and process the response
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();
        int statusCode = response.getStatusCodeValue();
        log.info("Response status code: " + statusCode); 
        log.info("Response body: " + responseBody);

        // Complete user authentication if successful
        if (statusCode == 200) {
            String email = (String) parser.parseMap(responseBody).get("email");
            //String authToken = (String) 
            log.info("HTTP 200");
            log.info(email);


            var generatedToken = UUID.randomUUID().toString().replaceAll("-", "0");


            Optional<User> user = usersRepository.findByEmail(email);
            if(user.isPresent()) {
                usersRepository.updateToken(email, generatedToken);
            }else{
                usersRepository.save(email, generatedToken);
            }

            
            //usersRepository.saveToken(googleMail, generatedToken);
            Optional<User> user1 = usersRepository.findByEmail(email);
            return user1.get().getId().toString() +":" + generatedToken;

            
            // return getUserName(Id);
        } else {
            //ЭТО ПРИМЕР ЗАПРОСА К БАЗЕ ДАННЫХ В ДЖАВА СКРИПТ ДРУГОГО ПРОЕКТА, НО ОЧЕНЬ ПОМОЖЕТ ВАМ! ОНО ДОБАВЛЯЕТ НОВОГО ЮЗЕРА,
            //ЕСЛИ ОН ЗАХОДИТ ВПЕРВЫЕ, А ЕСЛИ НЕ ВПЕРВЫЕ, ТО ОНО ПРОСТО ПЕРЕГЕНЕРИРУЕТ ТОКЕН:)
            //`INSERT INTO users (email, auth_token)
            //VALUES (:sdfsdf, :asdfs)
            //ON DUPLICATE KEY UPDATE 
            //auth_tempCode = ?, auth_token = ? `
            return "failure";
        }
    }

    // @PostMapping(path = "/{id}", consumes = "application/json")
    // @ResponseStatus(HttpStatus.CREATED)
    // public Long setUserId(@PathVariable("id") Long id, @RequestBody User userPatch) {
    //     User user = usersRepository.findById(id).get();
    //         user.setId(userPatch.getId());
    //         return user.getId();
    // }
}