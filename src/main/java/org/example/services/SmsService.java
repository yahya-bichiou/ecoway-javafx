package org.example.services;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;

public class SmsService {

    private static final String VONAGE_API_KEY = "d8069088";
    private static final String VONAGE_API_SECRET = "rFzd0FSwYicgdvGy";

    private final VonageClient client;

    public SmsService() {
        client = VonageClient.builder()
                .apiKey(VONAGE_API_KEY)
                .apiSecret(VONAGE_API_SECRET)
                .build();
    }

    public void sendSms(String toPhoneNumber, String messageContent) {
        TextMessage message = new TextMessage(
                "TaBoutique", // Sender name (alphanumérique max 11 caractères, ou ton numéro)
                toPhoneNumber,
                messageContent
        );

        try {
            SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                System.out.println("✅ SMS envoyé avec succès !");
            } else {
                System.out.println("❌ Échec envoi SMS : " + response.getMessages().get(0).getErrorText());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur : " + e.getMessage());
        }
    }
}
