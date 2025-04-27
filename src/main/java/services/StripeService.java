package services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeService {

    public StripeService() {
        Stripe.apiKey = "sk_test_51QxrSlQ0tlMYUYFG3svFEPUptdTB8YlUeRyDrsna48C9XrG0By7ePAmEnU62PBfAFJajYZqKCacYf6ShjuZjMZhe00hKMTzIb7"; // Your secret key here
    }

    public StripeSessionResponse createCheckoutSession(long amount) throws StripeException {
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("https://stripe-success.netlify.app/")
                        .setCancelUrl("https://stripe.com/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("usd")
                                                        .setUnitAmount(amount)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Test Product")
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        Session session = Session.create(params);
        return new StripeSessionResponse(session.getUrl(), session.getId());
    }

    public boolean isSessionPaid(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        System.out.println(session.getPaymentStatus());
        return "paid".equals(session.getPaymentStatus());
    }

}
