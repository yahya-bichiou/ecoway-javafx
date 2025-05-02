package org.example.services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SightengineService {

    private static final String API_USER = "1247911398";
    private static final String API_SECRET = "rqZsE8t3AeQCTgd7UUZ7VmMojr5xLkFT";

    public static String checkComment(String text, String lang) throws IOException {
        // ✅ Sécuriser la langue au début
        if (lang == null || (!lang.equalsIgnoreCase("en") && !lang.equalsIgnoreCase("fr"))) {
            lang = "en"; // par défaut anglais si pas bon
        }

        // ✅ Encoder proprement
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

        String urlParameters = "api_user=" + API_USER +
                "&api_secret=" + API_SECRET +
                "&text=" + encodedText +
                "&lang=" + lang +
                "&mode=standard";

        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        String requestURL = "https://api.sightengine.com/1.0/text/check.json";

        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setUseCaches(false);

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Erreur API Sightengine : HTTP " + responseCode);
            }

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {}
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
