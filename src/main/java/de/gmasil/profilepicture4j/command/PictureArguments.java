/*
 * ProfilePicture4J
 * Copyright © 2023 gmasil
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
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.profilepicture4j.impl.DefaultPictureGenerator;
import de.gmasil.profilepicture4j.utils.PictureUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import picocli.CommandLine.Option;

@Getter
@Setter
@NoArgsConstructor
public class PictureArguments {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Option(names = { "-n",
            "--name" }, description = "Full name to generate initials.", required = true)
    private String name;

    @Option(names = { "-o", "--output" }, description = "Output image file.")
    private File outputFile = new File("out.png");

    @Option(names = { "--output-format" }, description = "Output format of image (png, jpg, etc.).")
    private String outputFormat = "png";

    @Option(names = { "--color" }, description = "Primary color.")
    private String primaryColorString = "#1a57ba";

    @Option(names = { "--secondary-color" }, description = "Secondary color.")
    private String secondaryColorString = "#3477e3";

    @Option(names = { "--background-color" }, description = "Background color.")
    private String backgroundColorString = "#e6ebf2";

    @Option(names = { "--width" }, description = "Width of image in pixels.")
    private int width = 512;

    @Option(names = { "--height" }, description = "Height of image in pixels.")
    private int height = 512;

    @Option(names = { "--space" }, description = "Space to the borders of the image.")
    private Integer space = null;

    @Option(names = { "--ring-width" }, description = "Width of the ring.")
    private Integer ringWidth = null;

    @Option(names = { "--font-size" }, description = "Font size for the initials.")
    private Integer initialsFontSize = null;

    @Option(names = { "--font-height-correction" }, description = "Correction for font height for the initials.")
    private int initialsFontHeightCorrection = 0;

    @Option(names = { "--font-width-correction" }, description = "Correction for font height for the initials.")
    private int initialsFontWidthCorrection = 0;

    @Option(names = { "--font-family" }, description = "Font family for the initials.")
    private String initialsFontFamily = "Arial";

    @Option(names = { "--font-file" }, description = "Load font from file for the initials.")
    private File initialsFontFile;

    @Option(names = { "--debug" }, description = "Enable visual debug options.")
    private boolean debug;

    @Option(names = { "-v", "--verbose" }, description = "Log verbose.")
    private boolean verbose;

    @Option(names = {
            "--generator-class" }, description = "Specify fully qualified class name of picture generator to use.")
    private String generatorClass = null;

    private String initials;
    private Color primaryColor;
    private Color secondaryColor;
    private Color backgroundColor;
    private int shortestSide;

    public void postProcess() {
        initials = generateInitials();
        verboseLog("Using initials {}", initials);
        primaryColor = PictureUtils.colorFromHex(getPrimaryColorString());
        secondaryColor = PictureUtils.colorFromHex(getSecondaryColorString());
        backgroundColor = PictureUtils.colorFromHex(getBackgroundColorString());
        shortestSide = Math.min(getWidth(), getHeight());
        if (initialsFontFile != null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font customFont;
            try {
                customFont = Font.createFont(Font.TRUETYPE_FONT, initialsFontFile);
            } catch (Exception e) {
                throw new IllegalStateException("Error while loading font from " + initialsFontFile, e);
            }
            initialsFontFamily = customFont.getFontName();
            verboseLog("Setting font family to ", initialsFontFamily);
            ge.registerFont(customFont);
        }
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
        if (generatorClass == null) {
            generatorClass = DefaultPictureGenerator.class.getName();
            verboseLog("Using {} as picture generator", generatorClass);
        }
    }

    private String generateInitials() {
        return String.join("",
                Arrays.stream(getName().split(" ")).map(s -> s.substring(0, 1).toUpperCase()).toList());
    }

    private void verboseLog(String format, Object... logArgs) {
        if (verbose) {
            LOG.info(format, logArgs);
        }
    }
}
