package services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    public static final String ACCOUNT_SID = "AC4d8bf08758159a52a98d245462941f18";
    public static final String AUTH_TOKEN = "e1bf36cc4e824523f3917c844f8f8415";

    public static void sendSms(String to, String body) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber("+16202052905"),
                body
        ).create();

        System.out.println("Message sent: " + message.getSid());
    }
}

