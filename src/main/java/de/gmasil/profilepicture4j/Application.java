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
package de.gmasil.profilepicture4j;

import de.gmasil.profilepicture4j.command.PictureCommand;
import de.gmasil.profilepicture4j.command.UnsortedSynopsisHelpFactory;
import picocli.CommandLine;

public class Application {

    public static void main(String... args) {
        System.exit(runCmd(args));
    }

    public static int runCmd(String... args) {
        return new CommandLine(new PictureCommand())
                .setHelpFactory(new UnsortedSynopsisHelpFactory())
                .execute(args);
    }
}
