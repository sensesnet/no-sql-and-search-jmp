package org.epam.jmp;

import org.epam.jmp.controllers.request.RequestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
        var requestController = context.getBean(RequestController.class);
        requestController.startListenUserRequests();
    }
}