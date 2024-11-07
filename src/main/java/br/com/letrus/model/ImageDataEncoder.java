package br.com.letrus.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Component
public class ImageDataEncoder {

    public String encodeImageToBase64(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public String makeImageData(File file) throws IOException {
        String encodedImage = encodeImageToBase64(file);

        JSONObject image = new JSONObject();
        image.put("content", encodedImage);

        JSONObject feature = new JSONObject();
        feature.put("type", "DOCUMENT_TEXT_DETECTION");
        feature.put("maxResults", 1);

        JSONArray featuresArray = new JSONArray();
        featuresArray.put(feature);

        JSONObject imageRequest = new JSONObject();
        imageRequest.put("image", image);
        imageRequest.put("features", featuresArray);

        JSONObject requestData = new JSONObject();
        requestData.put("requests", new JSONArray().put(imageRequest));

        return requestData.toString();
    }
}
