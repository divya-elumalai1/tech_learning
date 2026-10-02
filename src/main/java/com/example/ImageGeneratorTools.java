package com.example;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageGeneratorTools {

    @Tool("Generates a stylish PNG badge image with the requested text and saves it locally")
    public String generateTextImage(
            @P("The exact text to render on the badge") String textToRender
    ) {
        System.out.println("\n>>> [TOOL CALLED] Rendering text: \"" + textToRender + "\"");

        int width = 640;
        int height = 220;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable anti-aliasing for clean text and curved edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Dark card background
        g2d.setColor(new Color(15, 23, 42)); // Slate dark
        g2d.fillRoundRect(12, 12, width - 24, height - 24, 28, 28);

        // Cyan-indigo gradient border
        g2d.setColor(new Color(56, 189, 248)); // Cyan
        g2d.setStroke(new BasicStroke(3.5f));
        g2d.drawRoundRect(12, 12, width - 24, height - 24, 28, 28);

        // Text styling
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 28));

        // Exact center alignment
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(textToRender)) / 2;
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();

        g2d.drawString(textToRender, x, y);
        g2d.dispose();

        File outputFile = new File("generated_badge.png");
        try {
            ImageIO.write(image, "PNG", outputFile);
            return "SUCCESS: Image successfully saved at " + outputFile.getAbsolutePath();
        } catch (IOException e) {
            return "ERROR: Failed to write image: " + e.getMessage();
        }
    }
}