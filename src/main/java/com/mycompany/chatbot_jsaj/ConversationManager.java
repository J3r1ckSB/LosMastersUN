package com.mycompany.chatbot_jsaj;

import java.util.ArrayList;
import java.util.List;

public class ConversationManager {

    private OllamaAPIConnector apiConnector;
    private chatc chatWindow;
    private List<Message> messageHistory;

    public ConversationManager(chatc chatWindow) {
        this.apiConnector = new OllamaAPIConnector();
        this.chatWindow = chatWindow;
        this.messageHistory = new ArrayList<>();
    }

    public void sendMessage(String userMessage) {
        // Agregar el mensaje del usuario al historial
        messageHistory.add(new Message("user", userMessage));

        try {
            // Enviar el mensaje a la API y obtener la respuesta
            String botResponse = apiConnector.sendMessageToAPI(userMessage);

            // Agregar la respuesta del bot al historial
            messageHistory.add(new Message("bot", botResponse));

            // Mostrar la respuesta en la ventana del chat
            chatWindow.chatHistory.append("Chatbot: " + botResponse + "\n");
        } catch (Exception e) {
            chatWindow.chatHistory.append("Chatbot: Error al comunicarse con la API.\n");
            e.printStackTrace();
        }
    }

    // Método para obtener el historial de mensajes
    public List<Message> getMessageHistory() {
        return messageHistory;
    }
}
