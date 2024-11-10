package com.mycompany.chatbot_jsaj;

public class Message {
    private String sender;   // El remitente del mensaje: "user" o "bot"
    private String content;  // El contenido del mensaje

    // Constructor para inicializar el mensaje
    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    // M�todo para obtener qui�n envi� el mensaje
    public String getSender() {
        return sender;
    }

    // M�todo para obtener el contenido del mensaje
    public String getContent() {
        return content;
    }

    // M�todo para devolver el mensaje en formato de texto
    @Override
    public String toString() {
        return sender + ": " + content;
    }
}
