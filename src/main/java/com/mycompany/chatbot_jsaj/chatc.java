package com.mycompany.chatbot_jsaj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class chatc extends JFrame {

    public JTextArea chatHistory;
    private JTextField userInput;
    private JButton sendButton;
    private JButton newChatButton;
    private JButton saveConversationButton;
    private OllamaAPIConnector apiConnector;
    private ConversationManager conversationManager;

    public chatc() {
        setTitle("Chatbot con Ollama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Inicializar el conector de la API
        apiConnector = new OllamaAPIConnector();

        // Inicializar el ConversationManager
        conversationManager = new ConversationManager(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatHistory);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        userInput = new JTextField(30);
        sendButton = new JButton("Enviar");
        newChatButton = new JButton("Nuevo Chat");
        saveConversationButton = new JButton("Guardar Conversación");
        inputPanel.add(userInput);
        inputPanel.add(sendButton);
        inputPanel.add(newChatButton);
        inputPanel.add(saveConversationButton);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = userInput.getText();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    userInput.setText("");
                }
            }
        });
        newChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Limpiar el historial de chat en la interfaz gráfica
                chatHistory.setText("");

                // Reiniciar el historial en el API Connector
                apiConnector.resetChat();

                // Reiniciar el historial en el ConversationManager (y actualizar la referencia en chatc)
                conversationManager = new ConversationManager(chatc.this);
            }
        });
        saveConversationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarConversacion();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu historialMenu = new JMenu("Historial");
        JMenuItem abrirHistorial = new JMenuItem("Abrir Historial");
        abrirHistorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HistorialFrame();
            }
        });
        historialMenu.add(abrirHistorial);
        menuBar.add(historialMenu);
        setJMenuBar(menuBar);

        add(mainPanel);
        setVisible(true);
    }

    private void sendMessage(String message) {
        chatHistory.append("Usuario: " + message + "\n");
        conversationManager.sendMessage(message);
    }

    // Método para guardar la conversación en un archivo JSON
    private void guardarConversacion() {
        JSONArray jsonArray = new JSONArray();

        // Leer el historial existente si el archivo existe
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("historial_chat.json")) {
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                jsonArray = (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            // Ignorar la excepción si el archivo no existe
        }

        // Agregar un separador si ya hay conversaciones guardadas
        if (!jsonArray.isEmpty()) {
            JSONObject separador = new JSONObject();
            separador.put("rol", "system");
            separador.put("mensaje", "Nueva Conversación");
            jsonArray.add(separador);
        }

        // Agregar los mensajes de la conversación actual
        for (Message message : conversationManager.getMessageHistory()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rol", message.getSender());
            jsonObject.put("mensaje", message.getContent());
            jsonArray.add(jsonObject);
        }

        try (FileWriter file = new FileWriter("historial_chat.json")) {
            file.write(jsonArray.toJSONString());
            JOptionPane.showMessageDialog(this, "Conversación guardada en historial_chat.json", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la conversación.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
