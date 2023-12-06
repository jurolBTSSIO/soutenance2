package fr.cda.tool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MailTest {

    @Test
    void sendEmail() {
        assertNotNull(Mail.sendEmail("julienrolland@gmx.fr", "rolland", "annonces"));
    }
}