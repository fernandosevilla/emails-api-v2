package dev.fernandosevilla.emailsapiv2.emails.infrastructure.config;

import dev.fernandosevilla.emailsapiv2.emails.application.port.in.SendEmailCommand;
import dev.fernandosevilla.emailsapiv2.emails.application.port.in.SendEmailUseCase;
import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailMessageRepositoryPort;
import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailSenderPort;
import dev.fernandosevilla.emailsapiv2.emails.infrastructure.mail.fake.FakeEmailSenderAdapter;
import dev.fernandosevilla.emailsapiv2.emails.infrastructure.persistence.inmemory.InMemoryEmailMessageRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        EmailBeanConfiguration.class,
        EmailBeanConfigurationTest.TestRepositoryConfiguration.class
})
class EmailBeanConfigurationTest {

    @Autowired
    private SendEmailUseCase sendEmailUseCase;

    @Autowired
    private EmailMessageRepositoryPort emailMessageRepositoryPort;

    @Autowired
    private EmailSenderPort emailSenderPort;

    @Test
    void shouldCreateSendEmailUseCaseBean() {
        assertThat(sendEmailUseCase).isNotNull();
    }

    @Test
    void shouldUseFakeEmailSenderAdapter() {
        assertThat(emailSenderPort)
                .isInstanceOf(FakeEmailSenderAdapter.class);
    }

    @Test
    void shouldCreateEmailMessageRepositoryPortBeanForTestContext() {
        assertThat(emailMessageRepositoryPort).isNotNull();
    }

    @Test
    void shouldSendEmailUsingConfiguredAdapters() {
        UUID emailId = sendEmailUseCase.send(new SendEmailCommand(
                "cliente@cliente.com",
                "Bienvenido",
                "Hola Fernando"
        ));

        assertThat(emailId).isNotNull();
    }

    @Configuration
    static class TestRepositoryConfiguration {

        @Bean
        EmailMessageRepositoryPort emailMessageRepositoryPort() {
            return new InMemoryEmailMessageRepositoryAdapter();
        }
    }
}
