package com.example.firstservice;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/first-service/")
public class FirstServiceController {

    Environment env;

    @Autowired
    public FirstServiceController(Environment env){
        this.env = env;
    }


    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome First Service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header) {
        log.info(header);
        return "Message, Hello World in First Service";
    }


    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server port={}", request.getServerPort());

        return String.format("Check, Hello World in First Service on Port %s", env.getProperty("local.server.port"));
    }


}
