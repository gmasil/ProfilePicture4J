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
package de.gmasil.profilepicture4j.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Random;

import de.gmasil.profilepicture4j.api.IPictureGenerator;
import de.gmasil.profilepicture4j.command.PictureArguments;

public class DefaultPictureGenerator implements IPictureGenerator {

    @Override
    public RenderedImage generatePicture(PictureArguments args) {
        BufferedImage img = new BufferedImage(args.getWidth(), args.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // background
        g.setColor(args.getBackgroundColor());
        g.fillRect(0, 0, args.getWidth(), args.getHeight());
        // circle stuff
        drawUniqueCircle(g, args.getWidth() / 2, args.getWidth() / 2, args.getShortestSide() / 2 - args.getSpace(),
                args);
        g.setColor(args.getBackgroundColor());
        drawDot(g, args.getWidth() / 2, args.getWidth() / 2,
                args.getShortestSide() / 2 - args.getSpace() - args.getRingWidth());
        // initials
        g.setColor(args.getPrimaryColor());
        g.setFont(new Font(args.getInitialsFontFamily(), Font.BOLD, args.getInitialsFontSize()));
        int initialsTextWidth = g.getFontMetrics().stringWidth(args.getInitials());
        int initialsTextHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent()
                - g.getFontMetrics().getLeading();
        g.drawString(args.getInitials(), args.getWidth() / 2 - initialsTextWidth / 2,
                args.getHeight() / 2 + initialsTextHeight / 2 - args.getInitialsFontHeightCorrection());
        if (args.isDebug()) {
            // red centerlines
            g.setColor(Color.RED);
            g.drawLine(0, args.getHeight() / 2, args.getWidth(), args.getHeight() / 2);
            g.drawLine(args.getWidth() / 2, 0, args.getWidth() / 2, args.getHeight());
        }
        return img;
    }

    private void drawUniqueCircle(Graphics2D g, int x, int y, int radius, PictureArguments args) {
        Random r = new Random(args.getName().hashCode());
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
                g.setColor(args.getPrimaryColor());
            } else {
                g.setColor(args.getSecondaryColor());
            }
            drawCirclePart(g, x, y, radius, lastAngle, angle);
            lastAngle += angle;
        }
    }

    public void drawDot(Graphics2D g, int x, int y, int radius) {
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public void drawCirclePart(Graphics2D g, int x, int y, int radius, int start, int angle) {
        g.fillArc(x - radius, y - radius, radius * 2, radius * 2, start, angle);
    }
}
