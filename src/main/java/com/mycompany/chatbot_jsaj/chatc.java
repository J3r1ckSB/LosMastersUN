package com.mycompany.chatbot_jsaj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private JList<String> historyList;
    private DefaultListModel<String> listModel;
    private OllamaAPIConnector apiConnector;
    private ConversationManager conversationManager;

    public chatc() {
        setTitle("Chatbot con Ollama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        apiConnector = new OllamaAPIConnector();
        conversationManager = new ConversationManager(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatHistory);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        userInput = new JTextField(30);
        sendButton = new JButton("Enviar");
        newChatButton = new JButton("Nuevo Chat");
        saveConversationButton = new JButton("Guardar Conversación");

        inputPanel.add(userInput);
        inputPanel.add(sendButton);
        inputPanel.add(newChatButton);
        inputPanel.add(saveConversationButton);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        listModel = new DefaultListModel<>();
        historyList = new JList<>(listModel);
        loadConversationHistory(); // Cargar el historial al iniciar
        JScrollPane historyScrollPane = new JScrollPane(historyList);
        historyScrollPane.setPreferredSize(new Dimension(200, 0));
        mainPanel.add(historyScrollPane, BorderLayout.WEST);

        historyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedConversation = historyList.getSelectedValue();
                if (selectedConversation != null) {
                    loadConversation(selectedConversation);
                }
            }
        });

        sendButton.addActionListener(e -> {
            String message = userInput.getText();
            if (!message.isEmpty()) {
                sendMessage(message);
                userInput.setText("");
            }
        });

        newChatButton.addActionListener(e -> {
            chatHistory.setText("");
            apiConnector.resetChat(); 
            conversationManager = new ConversationManager(chatc.this);
        });

        saveConversationButton.addActionListener(e -> guardarConversacion());

        add(mainPanel);
        setVisible(true);
    }

    private void sendMessage(String message) {
        chatHistory.append("Usuario: " + message + "\n");
        conversationManager.sendMessage(message);
    }

    public void guardarConversacion() {
        // Generar un nombre único para la conversación (usando la fecha y hora)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String nombreConversacion = "Conversación_" + dateFormat.format(new Date());

        // Obtener el historial de la conversación
        JSONArray historial = conversationManager.getHistorialComoJSONArray();

        // Guardar el historial en un archivo JSON
        try (FileWriter archivo = new FileWriter(nombreConversacion + ".json")) {
            archivo.write(historial.toJSONString());
            archivo.flush();
            System.out.println("Historial guardado en " + nombreConversacion + ".json");
        } catch (IOException e) {
            System.err.println("Error al guardar el historial: " + e.getMessage());
            // Manejo adicional de errores, como mostrar un mensaje al usuario
        }

        // Agregar el nombre de la conversación a la lista
        listModel.addElement(nombreConversacion);
    }

    private void loadConversationHistory() {
        listModel.clear();
        File carpeta = new File("."); // Obtener la carpeta actual
        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".json")); // Filtrar archivos .json

        if (archivos != null) {
            for (File archivo : archivos) {
                listModel.addElement(archivo.getName().replace(".json", ""));
            }
        }
    }

    private void loadConversation(String nombreConversacion) {
        chatHistory.setText(""); // Limpiar el área de chat
        try (FileReader reader = new FileReader(nombreConversacion + ".json")) {
            JSONParser parser = new JSONParser();
            JSONArray historialArray = (JSONArray) parser.parse(reader);

            for (Object obj : historialArray) {
                JSONObject mensaje = (JSONObject) obj;
                String rol = (String) mensaje.get("rol");
                String contenido = (String) mensaje.get("mensaje");
                chatHistory.append(rol + ": " + contenido + "\n");
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error al cargar la conversación: " + e.getMessage());
            // Manejo adicional de errores, como mostrar un mensaje al usuario
        }
    }

    public static void main(String[] args) {
        // Crear una instancia de la clase chatc para mostrar la ventana del chatbot.
        chatc chatWindow = new chatc();
    }
}