/**
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

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static String version = "dev";
    private static String revision = "0000000";

    public static void main(String[] args) throws IOException {
        readManifest();
        LOG.info("App version: {} (revision: {})", version, revision);
        LOG.info("Press enter to continue...");
        System.in.read();
    }

    private static void readManifest() throws IOException {
        Enumeration<URL> resources = Application.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
        while (resources.hasMoreElements()) {
            try {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                String ret = manifest.getMainAttributes().getValue("Version");
                if (ret != null) {
                    version = ret;
                }
                ret = manifest.getMainAttributes().getValue("SCM-Revision");
                if (ret != null) {
                    revision = ret;
                }
            } catch (IOException e) {
                // Nothing to do
            }
        }
    }
}
