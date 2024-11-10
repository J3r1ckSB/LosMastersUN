package com.mycompany.chatbot_jsaj;

public class Message {
    private String sender;   // El remitente del mensaje: "user" o "bot"
    private String content;  // El contenido del mensaje

    // Constructor para inicializar el mensaje
    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    // Método para obtener quién envió el mensaje
    public String getSender() {
        return sender;
    }

    // Método para obtener el contenido del mensaje
    public String getContent() {
        return content;
    }

    // Método para devolver el mensaje en formato de texto
    @Override
    public String toString() {
        return sender + ": " + content;
    }
}
