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

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.profilepicture4j.api.IPictureGenerator;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(name = "ProfilePicture4J.exe", mixinStandardHelpOptions = true, versionProvider = ManifestVersionProvider.class, sortOptions = false)
public class PictureCommand implements Callable<Integer> {

    public static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Mixin
    private PictureArguments args;

    @Override
    public Integer call() {
        args.postProcess();
        Class<?> generatorClass;
        try {
            generatorClass = Class.forName(args.getGeneratorClass());
        } catch (ClassNotFoundException e) {
            LOG.error("The generator class {} does not exist", args.getGeneratorClass());
            return 21;
        }
        Object generatorInstance;
        try {
            generatorInstance = generatorClass.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            LOG.error("The generator class {} does not have a default constructor", args.getGeneratorClass());
            return 23;
        } catch (Exception e) {
            LOG.error("Error while instanciating generator class {}\n{}", args.getGeneratorClass(), e);
            return 25;
        }
        if (generatorInstance instanceof IPictureGenerator generator) {
            RenderedImage img = generator.generatePicture(args);
            try {
                ImageIO.write(img, args.getOutputFormat(), args.getOutputFile());
            } catch (IOException e) {
                throw new IllegalStateException(
                        "Error while saving picture to " + args.getOutputFile().getAbsolutePath(), e);
            }
            return 0;
        } else {
            LOG.error("The generator class {} is expected to implement {}", args.getGeneratorClass(),
                    IPictureGenerator.class.getName());
            return 27;
        }
    }
}
