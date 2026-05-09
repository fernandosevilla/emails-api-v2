package dev.fernandosevilla.emailsapiv2.emails.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailAddressTest {
    @Test
    void shouldNormalizeEmailAddress() {
        EmailAddress emailAddress = new EmailAddress("  FERnando@GmaIl.com  ");

        assertThat(emailAddress.value()).isEqualTo("fernando@gmail.com");
    }

    @Test
    void shouldRejecctNullEmailAddress() {
        assertThatThrownBy(() -> new EmailAddress(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Email address cannot be null");
    }

    @Test
    void shouldRejectBlankEmailAddress() {
        assertThatThrownBy(() -> new EmailAddress(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email address cannot be blank");
    }

    @Test
    void shouldRejectEmailAddressWithoutAtSymbol() {
        assertThatThrownBy(() -> new EmailAddress("fernandogmail.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email address must be valid");
    }
}
