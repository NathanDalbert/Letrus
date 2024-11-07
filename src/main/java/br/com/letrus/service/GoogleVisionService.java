package br.com.letrus.service;

import br.com.letrus.model.ImageDataEncoder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class GoogleVisionService {

    @Value("${google.api.key}")
    private String apiKey;

    private final ImageDataEncoder encoder;

    @Autowired
    public GoogleVisionService(ImageDataEncoder encoder) {
        this.encoder = encoder;
    }

    public String requestOCR(File file) throws IOException {
        String imageData = encoder.makeImageData(file);

        URL url = new URL("https://vision.googleapis.com/v1/images:annotate?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = imageData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray responses = jsonResponse.getJSONArray("responses");
            if (responses.length() > 0 && responses.getJSONObject(0).has("textAnnotations")) {
                return responses.getJSONObject(0)
                        .getJSONArray("textAnnotations")
                        .getJSONObject(0)
                        .getString("description");
            } else {
                return "Nenhum texto encontrado.";
            }
        } else {
            return "Erro ao realizar OCR. Código de resposta: " + responseCode;
        }
    }
}
