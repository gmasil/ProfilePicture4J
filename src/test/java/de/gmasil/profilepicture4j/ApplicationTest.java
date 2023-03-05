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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.io.FileMatchers.aFileWithSize;
import static org.hamcrest.io.FileMatchers.anExistingFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testNoArgsProduceNonZeroExitCode() throws Exception {
        int status = Application.runCmd();
        assertThat(status, is(equalTo(2)));
        assertThat(outContent.toString(), is(emptyString()));
        assertThat(errContent.toString(), containsString("Missing required option"));
        assertThat(errContent.toString(), containsString("Usage:"));
    }

    @Test
    void testHelpPageProduceZeroExitCode() throws Exception {
        int status = Application.runCmd("-h");
        assertThat(status, is(equalTo(0)));
        assertThat(outContent.toString(), not(containsString("Missing required option")));
        assertThat(outContent.toString(), containsString("Usage:"));
        assertThat(errContent.toString(), is(emptyString()));
    }

    @Test
    void testApplicationCreatesImageWithOnlyNameArgumentSet() throws Exception {
        File file = new File("target", "example.png");
        int status = Application.runCmd("-n", "John Doe", "-o", file.getCanonicalPath());
        assertThat(status, is(equalTo(0)));
        assertThat(file, anExistingFile());
        assertThat(file, aFileWithSize(greaterThan(15_000L)));
        assertThat(outContent.toString(), is(emptyString()));
        assertThat(errContent.toString(), is(emptyString()));
    }

    @Test
    void testApplicationVersionIsSet() throws Exception {
        int status = Application.runCmd("--version");
        assertThat(status, is(equalTo(0)));
        assertThat(outContent.toString(), containsString("Version:"));
        assertThat(outContent.toString(), containsString("Revision:"));
        assertThat(errContent.toString(), is(emptyString()));
    }
}
