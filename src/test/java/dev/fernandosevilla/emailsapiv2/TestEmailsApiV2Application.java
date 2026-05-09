package dev.fernandosevilla.emailsapiv2;

import org.springframework.boot.SpringApplication;

public class TestEmailsApiV2Application {

    public static void main(String[] args) {
        SpringApplication.from(EmailsApiV2Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
