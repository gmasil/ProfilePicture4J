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

import java.util.Collection;
import java.util.Comparator;

import picocli.CommandLine.Help;
import picocli.CommandLine.Help.ColorScheme;
import picocli.CommandLine.IHelpFactory;
import picocli.CommandLine.Model.ArgSpec;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;

public class UnsortedSynopsisHelpFactory implements IHelpFactory {

    @Override
    public Help create(CommandSpec commandSpec, ColorScheme colorScheme) {
        return new Help(commandSpec, colorScheme) {

            @Override
            protected Ansi.Text createDetailedSynopsisOptionsText(Collection<ArgSpec> done,
                    Comparator<OptionSpec> optionSort, boolean clusterBooleanOptions) {
                return super.createDetailedSynopsisOptionsText(done, null, clusterBooleanOptions);
            }
        };
    }
}
