package com.mycompany.chatbot_jsaj;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConversationManager {
    private final chatc chatWindow;
    private final List<Message> messageHistory;

    public ConversationManager(chatc chatWindow) {
        this.chatWindow = chatWindow;
        this.messageHistory = new ArrayList<>();
    }

    public void sendMessage(String message) {
        messageHistory.add(new Message("Usuario", message));
        // ... (resto del código para enviar el mensaje a la API y recibir la respuesta) ...
    }

    public List<Message> getMessageHistory() {
        return messageHistory;
    }

    // Método para obtener el historial como JSONArray
    public JSONArray getHistorialComoJSONArray() {
        JSONArray historial = new JSONArray();
        for (Message mensaje : messageHistory) {
            JSONObject jsonMensaje = new JSONObject();
            jsonMensaje.put("rol", mensaje.getSender());
            jsonMensaje.put("mensaje", mensaje.getContent());
            historial.add(jsonMensaje);
        }
        return historial;
    }
}
