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
package de.gmasil.profilepicture4j.manifest;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

import de.gmasil.profilepicture4j.Application;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ManifestData {

    private String version;
    private String revision;

    @Override
    public String toString() {
        return String.format("Version %s (%s)", getVersion(), getRevision());
    }

    public static ManifestData readManifest() throws IOException {
        String version = "dev";
        String revision = "0000000";
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
        return new ManifestData(version, revision);
    }

}
