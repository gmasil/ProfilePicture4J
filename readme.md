# ProfilePicture4J [![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fjenkins.gmasil.de%2Fjob%2Fgithub-gmasil%2Fjob%2FProfilePicture4J%2Fjob%2Fmaster%2F)](https://jenkins.gmasil.de/blue/organizations/jenkins/github-gmasil%2FProfilePicture4J/branches/) [![Quality Gate Status](https://sonar.gmasil.de/api/project_badges/measure?project=de.gmasil%3Aprofilepicture4j%3Amaster&metric=alert_status)](https://sonar.gmasil.de/dashboard?id=de.gmasil%3Aprofilepicture4j%3Amaster) [![Coverage](https://sonar.gmasil.de/api/project_badges/measure?project=de.gmasil%3Aprofilepicture4j%3Amaster&metric=coverage)](https://sonar.gmasil.de/dashboard?id=de.gmasil%3Aprofilepicture4j%3Amaster)

ProfilePicture4J is a simple tool to generate profile pictures using name initials while picking up your corporate identity colors.

This tool is written in Java 17 and can be executed as a bundled jar file on WIndows, Linux and Mac OS, but requires Java 17 or later to be installed. For Windows users there is a bundled executable including a JRE 17 that can run without Java installed on the system.

## Download

### Windows Bundle

The latest Windows bundle including the JRE can be downloaded here: [ProfilePicture4J.exe](https://jenkins.gmasil.de/job/github-gmasil/job/ProfilePicture4J/job/master/lastSuccessfulBuild/artifact/target/ProfilePicture4J.exe)

### Jar

The small OS independent jar file can be downloaded here: [ProfilePicture4J.jar](https://jenkins.gmasil.de/job/github-gmasil/job/ProfilePicture4J/job/master/lastSuccessfulBuild/artifact/target/ProfilePicture4J.jar)

## Usage

This is a command line tool and must be executed using any kind of terminal, shell, cmd etc.

If you use the Windows executable use

```bash
.\ProfilePicture4J.exe
```

and if you use the jar file

```bash
java -jar ProfilePicture4J.jar
```

*Hint: The following commands will only show the Windows executable style, but the program arguments are used the same way.*

At least the name has to be provided for the initials:

```bash
.\ProfilePicture4J.exe --name "John Doe"
```

The tool will generate different colors and different patterns depending on the full name. If you generate the profile picture with the same arguments, the result will always be the same. If you have employees with the same initials the tool will generate two different profile pictures for them. Below you can see the profile pictures for **John Doe** and **Jackson Dallas** using the color scheme of my [website](https://gmasil.de/):

![John Doe](examples/john_doe.png "John Doe")
![Jackson Dallas](examples/jackson_dallas.png "Jackson Dallas")

As you can see the striped ring differs between those two.

You can get a list of all arguments including a brief description when using the help parameter `-h`:

```bash
.\ProfilePicture4J.exe -h
```

```
Usage: ProfilePicture4J.exe [-hvV] [--debug]
                            [--background-color=<backgroundColorString>]
                            [--color=<primaryColorString>]
                            [--font-family=<initialsFontFamily>]
                            [--font-file=<initialsFontFile>]
                            [--font-height-correction=<initialsFontHeightCorrect
                            ion>] [--font-size=<initialsFontSize>]
                            [--font-width-correction=<initialsFontWidthCorrectio
                            n>] [--height=<height>] -n=<name> [-o=<outputFile>]
                            [--output-format=<outputFormat>]
                            [--ring-width=<ringWidth>]
                            [--secondary-color=<secondaryColorString>]
                            [--space=<space>] [--width=<width>]
      --background-color=<backgroundColorString>
                          Background color.
      --color=<primaryColorString>
                          Primary color.
      --debug             Enable visual debug options.
      --font-family=<initialsFontFamily>
                          Font family for the initials.
      --font-file=<initialsFontFile>
                          Load font from file for the initials.
      --font-height-correction=<initialsFontHeightCorrection>
                          Correction for font height for the initials.
      --font-size=<initialsFontSize>
                          Font size for the initials.
      --font-width-correction=<initialsFontWidthCorrection>
                          Correction for font height for the initials.
  -h, --help              Show this help message and exit.
      --height=<height>   Height of image in pixels.
  -n, --name=<name>       Full name to generate initials.
  -o, --output=<outputFile>
                          Output image file.
      --output-format=<outputFormat>
                          Output format of image (png, jpg, etc.).
      --ring-width=<ringWidth>
                          Width of the ring.
      --secondary-color=<secondaryColorString>
                          Secondary color.
      --space=<space>     Space to the borders of the image.
  -v, --verbose           Log verbose.
  -V, --version           Print version information and exit.
      --width=<width>     Width of image in pixels.
```
