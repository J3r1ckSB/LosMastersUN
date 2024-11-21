package com.mycompany.chatbot_jsaj;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChatBot {
    // Eliminar los arrays estáticos roles y mensajes

    public static void guardarEnJson() {
        JSONArray historial = new JSONArray();
        // Obtener el historial de la conversación desde ConversationManager
        // ...

        try (FileWriter archivo = new FileWriter("historial_chat.json")) {
            archivo.write(historial.toJSONString());
            archivo.flush();
            System.out.println("Historial guardado en historial_chat.json");
        } catch (IOException e) {
            System.err.println("Error al guardar el historial: " + e.getMessage());
            // Manejo adicional de errores, como mostrar un mensaje al usuario
        }
    }
}
