package com.example;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.File;
import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        String badgeText = readBadgeText(args);
        if (badgeText.isEmpty()) {
            System.out.println("No text entered. Exiting.");
            return;
        }

        System.out.println("Connecting to local Ollama (llama3.2)...");

        // 1. Configure the local Ollama model
        ChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3.2")
                .temperature(0.0) // 0.0 makes tool calling deterministic
                .timeout(Duration.ofMinutes(2))
                .build();

        // 2. Wire the AI service with our tool
        ImageAssistant assistant = AiServices.builder(ImageAssistant.class)
                .chatModel(model)
                .tools(new ImageGeneratorTools())
                .build();

        // 3. Prompt that triggers the tool
        String prompt = "Please create a badge PNG image with the text: '" + badgeText + "'. "
                + "After the tool succeeds, reply with only the local file path from the tool result. "
                + "Do not include a web URL or a markdown image.";
        System.out.println("User Prompt: " + prompt);

        // 4. Send request
        String response = assistant.chat(prompt);

        System.out.println("\nLlama 3.2 Response:\n" + response);
        File badge = new File("generated_badge.png");
        if (badge.isFile()) {
            System.out.println("Saved image: " + badge.getAbsolutePath());
        }
    }

    private static String readBadgeText(String[] args) {
        if (args.length > 0) {
            return String.join(" ", args).trim();
        }

        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        String text = JOptionPane.showInputDialog(
                frame,
                "Enter the text to put on the badge:",
                "Badge text",
                JOptionPane.PLAIN_MESSAGE);
        frame.dispose();
        return text == null ? "" : text.trim();
    }
}