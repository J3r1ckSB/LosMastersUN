package com.mycompany.chatbot_jsaj;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class OllamaAPIConnector {
    private final String apiUrl = "http://localhost:11434/api/chat";
    private final String model = "llama3.2";
    private JsonArray messages; // Para almacenar el historial de mensajes

    public OllamaAPIConnector() {
        this.messages = new JsonArray(); // Inicializar el array en el constructor
    }

    public String sendMessageToAPI(String userQuestion) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(apiUrl);
            request.addHeader("Content-Type", "application/json");

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", model);

            // Crear el objeto "message" para el usuario
            JsonObject userMessage = new JsonObject();
            userMessage.addProperty("role", "user");
            userMessage.addProperty("content", userQuestion);
            messages.add(userMessage); // Agregar el mensaje del usuario al historial

            // Crear el objeto "message" para el asistente
            JsonObject assistantMessage = new JsonObject();
            assistantMessage.addProperty("role", "assistant");
            assistantMessage.addProperty("content", ""); // El asistente no tiene contenido aún
            messages.add(assistantMessage); // Agregar el mensaje del asistente al historial

            requestBody.add("messages", messages);
            requestBody.addProperty("stream", false);

            StringEntity entity = new StringEntity(requestBody.toString(), StandardCharsets.UTF_8);
            request.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder responseString = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseString.append(line);
                }

                JsonObject jsonResponse = JsonParser.parseString(responseString.toString()).getAsJsonObject();

                String assistantResponse = jsonResponse.getAsJsonObject("message").get("content").getAsString();

                // Actualizar el contenido del mensaje del asistente en el historial
                assistantMessage.addProperty("content", assistantResponse);

                return assistantResponse;
            }
        }
    }

    // Método para reiniciar el historial de mensajes
    public void resetChat() {
        this.messages = new JsonArray();
    }
}
