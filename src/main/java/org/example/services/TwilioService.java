package org.example.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {
    public static final String ACCOUNT_SID = "ACe5b2fe08dbc829c8b524c29cf09879cd";
    public static final String AUTH_TOKEN = "8a2f5ad5c9f6380fc41955169e3db69e";
    public static final String FROM_NUMBER = "+13526757255"; // Ton numéro Twilio

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendSMS(String to, String body) {
        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(FROM_NUMBER),
                body
        ).create();

        System.out.println("Message SID : " + message.getSid());
    }
}
