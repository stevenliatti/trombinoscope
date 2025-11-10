# Trombinoscope

Scala programs and template LaTeX to make a face book (aka "trombinoscope" in french) from photos and CSV names file. The current usage is for a choir, the chorister are grouped by voice.

## Usage

Put in the directory `in` a CSV file (separated by a comma `,`), named by default `choeur-photos.csv` with the following header and structure :

```csv
lastname,name,no,voice
Smith,Mary,12,alto
```

Put in the directory `photos` your photos in the corresponding order in the CSV file (aka the `no` field).

A first program rename the images files (example: `IMG_123.jpg -> 1.jpg`) and convert it with imagemagick to reduce his size.

A second program read the CSV file and make a `.tex` file to compile with LaTeX.

A `makefile` is present to help through the different steps. It also define some variables.

You can use it simply by execute `make` in the root of project.

## Dependencies

The workflow is run on GNU/Linux OS. You will need [Scala](https://scala-lang.org/) (and possibly Java).

Scala 3.3.4 (via sdkman):

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
# If you need Java, for example 21 version
# sdk install java 21.0.8-tem
sdk install scala 3.3.4
```

Other dependencies (on Debian/Ubuntu based) :
```bash
sudo apt install texlive texlive-latex-extra texlive-lang-french make imagemagick sed
```

## Docker

A `Dockerfile` is present if you have [Docker](https://docs.docker.com/) or you don't want install all the dependencies or if you are on Windows or MacOS for example.

To build and run the container :

```bash
docker build -t trombinoscope .
docker run -it --name trombinoscope trombinoscope
```

Inside the container, you can `make` the pdf file. Once finished, exit the container (by typing `exit`) and execute the next commands in the host to retrieve the pdf file and remove the container :

```bash
docker cp trombinoscope:/root/trombinoscope/trombinoscope.pdf .
docker rm trombinoscope
```

