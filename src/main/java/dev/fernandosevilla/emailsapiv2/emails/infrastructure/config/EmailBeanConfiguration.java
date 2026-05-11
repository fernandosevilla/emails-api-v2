package dev.fernandosevilla.emailsapiv2.emails.infrastructure.config;

import dev.fernandosevilla.emailsapiv2.emails.application.port.in.SendEmailUseCase;
import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailMessageRepositoryPort;
import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailSenderPort;
import dev.fernandosevilla.emailsapiv2.emails.application.service.SendEmailService;
import dev.fernandosevilla.emailsapiv2.emails.infrastructure.mail.fake.FakeEmailSenderAdapter;
import dev.fernandosevilla.emailsapiv2.emails.infrastructure.persistence.inmemory.InMemoryEmailMessageRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de beans del módulo de emails.
 *
 * <p>
 *     Aqui conectamos los puertos de la aplicación con adaptadores
 *     concretos de infraestructura.
 * </p>
 */
@Configuration
public class EmailBeanConfiguration {
    /**
     * Crea el adpaptador de persistencia de emails.
     *
     * <p>
     *     Para desarrollo y pruebas de momento lo dejo en memoria.
     * </p>
     *
     * @return implementación del puerto de persistencia de mensajes de email
     */
    @Bean
    public EmailMessageRepositoryPort emailMessageRepositoryPort() {
        return new InMemoryEmailMessageRepositoryAdapter();
    }

    /**
     * Crea el adaptador de envío de emails.
     *
     * <p>
     *     Para desarrollo y pruebas de momento utilizo un adaptador fake
     *     que no envía emails, si no que registra logs.
     * </p>
     *
     * @return implementación del puerto de envío de emails
     */
    @Bean
    public EmailSenderPort emailSenderPort() {
        return new FakeEmailSenderAdapter();
    }

    /**
     * Crea el caso de uso de envío de emails.
     *
     * <p>
     *     El servicio recibe sus dependencias a través de los puertos
     *     de salida, manteniendo la capa de aplicación desacoplada de
     *     la infraestructura.
     * </p>
     *
     * @param emailMessageRepositoryPort puerto de persistencia de mensajes de email
     * @param emailSenderPort puerto de envío de emails
     *
     * @return caso de uso de envío de emails
     */
    @Bean
    public SendEmailUseCase sendEmailUseCase(
            EmailMessageRepositoryPort emailMessageRepositoryPort,
            EmailSenderPort emailSenderPort
    ) {
        return new SendEmailService(
                emailMessageRepositoryPort,
                emailSenderPort
        );
    }
}
