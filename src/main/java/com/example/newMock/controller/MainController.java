package com.example.newMock.controller;

import com.example.newMock.model.RequestDTO;
import com.example.newMock.model.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import java.util.concurrent.TimeUnit;

@RestController
public class MainController {
    private Logger log = LoggerFactory.getLogger(MainController.class);
    ObjectMapper mapper = new ObjectMapper(); //для логирования заглушки

    // для каждой команды из  HTTP есть своя аннотация:
    @PostMapping(
            //объявляем параментры аннотации:
            value = "/info/postBalances", // по какому адресу заглушка будет работать, на какой адрес отвечать
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )


    public Object postBalances(@RequestBody RequestDTO requestDTO) { //requestDTO - это уже экземпляр класса, в который попадает JSON, @RequestBody указывает, что является телом запроса
        try {
            String clientId = requestDTO.getClientId();
            String account = requestDTO.getAccount();
            String rqUID = requestDTO.getRqUID();
            char firstDigit = clientId.charAt(0);

            BigDecimal maxLimit;
            String currency;
            BigDecimal balance;


            if (firstDigit == '8') {
                maxLimit = new BigDecimal(2000);
                currency = "US";
            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal(1000);
                currency = "EU";
            } else {
                maxLimit = new BigDecimal(10000);
                currency = "RUB";
            }


            Random random = new Random();

            // Генерируем случайное число от 0.0 до 1.0
            double randomDouble = random.nextDouble();

            // Преобразуем в BigDecimal и умножаем на maxLimit
            BigDecimal randomValue = BigDecimal.valueOf(randomDouble);
            balance = (randomValue.multiply(maxLimit));

            //Приводим к  двумя знакам после запятой
            balance = balance.setScale(2, RoundingMode.HALF_UP);

            ResponseDTO responseDTO = new ResponseDTO();

            responseDTO.setRqUID(requestDTO.getRqUID());
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currency);
            responseDTO.setBalance(balance);
            responseDTO.setMaxLimit(maxLimit);

            log.error("***************** Logging of RequestDTO *****************" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("***************** Logging of ResponseDTO *****************" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            TimeUnit.MILLISECONDS.sleep(2000);

            return responseDTO;
        } //close try

        catch (Exception e) {
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }

        }//close postBalances
    }
}