package com.mycompany.chatbot_jsaj;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HistorialFrame extends JFrame {

    private JTextArea historialTextArea;

    public HistorialFrame() {
        setTitle("Historial de Chat");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        historialTextArea = new JTextArea();
        historialTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historialTextArea);
        add(scrollPane);

        cargarHistorial();

        setVisible(true);
    }

    private void cargarHistorial() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("historial_chat.json")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                String rol = (String) jsonObject.get("rol");
                String mensaje = (String) jsonObject.get("mensaje");
                historialTextArea.append(rol + ": " + mensaje + "\n");
            }
        } catch (IOException | ParseException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el historial. Debe guardar por lo menos una conversación (botón Guardar conversación)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}