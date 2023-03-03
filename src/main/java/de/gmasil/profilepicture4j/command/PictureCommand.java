/*
 * ProfilePicture4J
 * Copyright © ${project.inceptionYear} gmasil
 *
 * This file is part of ProfilePicture4J.
 *
 * ProfilePicture4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProfilePicture4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProfilePicture4J. If not, see <https://www.gnu.org/licenses/>.
 */
package de.gmasil.profilepicture4j.command;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "ProfilePicture4J.exe", mixinStandardHelpOptions = true, versionProvider = ManifestVersionProvider.class, sortOptions = false)
public class PictureCommand implements Callable<Integer> {

    public static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Option(names = { "-n",
            "--name" }, description = "Full name to generate initials.", required = true)
    String name;

    @Option(names = { "-o", "--output" }, description = "Output image file.")
    File outputFile = new File("out.png");

    @Option(names = { "--output-format" }, description = "Output format of image (png, jpg, etc.).")
    String outputFormat = "png";

    @Option(names = { "--color" }, description = "Primary color.")
    String primaryColorString = "#1a57ba";

    @Option(names = { "--secondary-color" }, description = "Secondary color.")
    String secondaryColorString = "#3477e3";

    @Option(names = { "--background-color" }, description = "Background color.")
    String backgroundColorString = "#e6ebf2";

    @Option(names = { "--width" }, description = "Width of image in pixels.")
    int width = 512;

    @Option(names = { "--height" }, description = "Height of image in pixels.")
    int height = 512;

    @Option(names = { "--space" }, description = "Space to the borders of the image.")
    Integer space = null;

    @Option(names = { "--ring-width" }, description = "Width of the ring.")
    Integer ringWidth = null;

    @Option(names = { "--font-size" }, description = "Font size for the initials.")
    Integer initialsFontSize = null;

    @Option(names = { "--font-height-correction" }, description = "Correction for font height for the initials.")
    int initialsFontHeightCorrection = 0;

    @Option(names = { "--font-width-correction" }, description = "Correction for font height for the initials.")
    int initialsFontWidthCorrection = 0;

    @Option(names = { "--font-family" }, description = "Font family for the initials.")
    String initialsFontFamily = "Arial";

    @Option(names = { "--font-file" }, description = "Load font from file for the initials.")
    File initialsFontFile;

    @Option(names = { "--debug" }, description = "Enable visual debug options.")
    boolean debug;

    @Option(names = { "-v", "--verbose" }, description = "Log verbose.")
    boolean verbose;

    private String initials;
    private Color primaryColor;
    private Color secondaryColor;
    private Color backgroundColor;
    private int shortestSide;

    @Override
    public Integer call() throws Exception {
        prepareValues();
        // image
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // background
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);
        // circle stuff
        drawUniqueCircle(g, width / 2, width / 2, shortestSide / 2 - space);
        g.setColor(backgroundColor);
        drawDot(g, width / 2, width / 2, shortestSide / 2 - space - ringWidth);
        // initials
        g.setColor(primaryColor);
        if (initialsFontFile != null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, initialsFontFile);
            initialsFontFamily = customFont.getFontName();
            ge.registerFont(customFont);
        }
        g.setFont(new Font(initialsFontFamily, Font.BOLD, initialsFontSize));
        int initialsTextWidth = g.getFontMetrics().stringWidth(initials);
        int initialsTextHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent()
                - g.getFontMetrics().getLeading();
        g.drawString(initials, width / 2 - initialsTextWidth / 2,
                height / 2 + initialsTextHeight / 2 - initialsFontHeightCorrection);
        if (debug) {
            // red centerlines
            g.setColor(Color.RED);
            g.drawLine(0, height / 2, width, height / 2);
            g.drawLine(width / 2, 0, width / 2, height);
        }
        ImageIO.write(img, outputFormat, outputFile);
        return 0;
    }

    public void prepareValues() {
        initials = getInitials();
        LOG.info("Using initials {}", initials);
        primaryColor = colorFromHex(primaryColorString);
        secondaryColor = colorFromHex(secondaryColorString);
        backgroundColor = colorFromHex(backgroundColorString);
        shortestSide = Math.min(width, height);
        if (space == null) {
            space = (int) (shortestSide * 0.07f);
            verboseLog("Setting space to {}", space);
        }
        if (ringWidth == null) {
            ringWidth = (int) (shortestSide * 0.07f);
            verboseLog("Setting ring-width to {}", ringWidth);
        }
        if (initialsFontSize == null) {
            initialsFontSize = (int) (shortestSide * 0.3f);
            verboseLog("Setting font-size to {}", initialsFontSize);
        }
    }

    public void drawUniqueCircle(Graphics2D g, int x, int y, int radius) {
        Random r = new Random(name.hashCode());
        int amount = 20;
        int variance = 50;
        int[] numbers = new int[amount];
        int total = 0;
        for (int i = 0; i < amount; i++) {
            numbers[i] = r.nextInt(10, 10 + variance);
            total += numbers[i];
        }
        if (total == 0) {
            throw new IllegalStateException("Cannot draw an empty circle");
        }
        int lastAngle = 0;
        for (int i = 0; i < amount; i++) {

            int angle = numbers[i] * 360 / total + 1;
            if (i % 2 == 0) {
                g.setColor(primaryColor);
            } else {
                g.setColor(secondaryColor);
            }
            drawCirclePart(g, x, y, radius, lastAngle, angle);
            lastAngle += angle;
        }
    }

    public Color colorFromHex(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        return new Color(Integer.parseInt(hex, 16));
    }

    public void drawDot(Graphics2D g, int x, int y, int radius) {
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public void drawCirclePart(Graphics2D g, int x, int y, int radius, int start, int angle) {
        g.fillArc(x - radius, y - radius, radius * 2, radius * 2, start, angle);
    }

    public String getInitials() {
        return String.join("", Arrays.stream(name.split(" ")).map(s -> s.substring(0, 1).toUpperCase()).toList());
    }

    private void verboseLog(String format, Object... args) {
        if (verbose) {
            LOG.info(format, args);
        }
    }
}
