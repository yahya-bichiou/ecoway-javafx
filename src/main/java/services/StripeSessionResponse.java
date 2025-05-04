package services;

public class StripeSessionResponse {
    private String url;
    private String sessionId;

    public StripeSessionResponse(String url, String sessionId) {
        this.url = url;
        this.sessionId = sessionId;
    }

    public String getUrl() {
        return url;
    }

    public String getSessionId() {
        return sessionId;
    }
}
