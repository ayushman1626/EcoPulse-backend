package com.ecopulse.EcoPulse_Backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("api/hello")
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok("Hello App is running ");
    }
}
