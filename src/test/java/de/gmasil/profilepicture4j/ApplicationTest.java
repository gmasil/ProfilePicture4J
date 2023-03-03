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
package de.gmasil.profilepicture4j;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.io.FileMatchers.aFileWithSize;
import static org.hamcrest.io.FileMatchers.anExistingFile;

import java.io.File;

import org.junit.jupiter.api.Test;

class ApplicationTest {

    @Test
    void testNoArgsProduceNonZeroExitCode() throws Exception {
        int status = Application.runCmd();
        assertThat(status, is(equalTo(2)));
    }

    @Test
    void testHelpPageProduceZeroExitCode() throws Exception {
        int status = Application.runCmd("-h");
        assertThat(status, is(equalTo(0)));
    }

    @Test
    void testApplicationCreatesImageWithOnlyNameArgumentSet() throws Exception {
        File file = new File("target", "example.png");
        int status = Application.runCmd("-n", "John Doe", "-o", file.getCanonicalPath());
        assertThat(status, is(equalTo(0)));
        assertThat(file, anExistingFile());
        assertThat(file, aFileWithSize(greaterThan(15_000L)));
    }
}
