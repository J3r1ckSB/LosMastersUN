package com.mycompany.chatbot_jsaj;

import java.io.FileWriter;
import java.io.IOException;

public class ChatBot {

    private static final int MAX_MENSAJES = 1000000;
    private static String[] roles = new String[MAX_MENSAJES];
    private static String[] mensajes = new String[MAX_MENSAJES];
    private static int indice = 0;

    public static void guardarMensaje(String rol, String mensaje) {
        if (indice < MAX_MENSAJES) {
            roles[indice] = rol;
            mensajes[indice] = mensaje;
            indice++;
        } else {
            System.out.println("Historial lleno, no se pueden guardar más mensajes.");
        }
    }

    public static void guardarEnJson() {
        try (FileWriter archivo = new FileWriter("historial_chat.json")) {
            archivo.write("[\n");
            for (int i = 0; i < indice; i++) {
                archivo.write("  {\n");
                archivo.write("    \"rol\": \"" + roles[i] + "\",\n");
                archivo.write("    \"mensaje\": \"" + mensajes[i] + "\"\n");
                archivo.write("  }");
                if (i < indice - 1) {
                    archivo.write(",\n");
                } else {
                    archivo.write("\n");
                }
            }
            archivo.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        chatc f = new chatc();
        f.setVisible(true);
    }
}
