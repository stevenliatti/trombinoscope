# Trombinoscope

Scala programs and template LaTeX to make a face book (aka "trombinoscope" in french) from photos and CSV names file. The current usage is for a choir, the chorister are grouped by voice.

## Usage

Put in the directory `in` a CSV file (separated by a comma `,`) with the following header :

```csv
lastname,name,no,voice
Smith,Mary,12,alto 1
```

Put in the directory `photos` your photos in the corresponding order in the CSV file (aka the `no` field).

A first program rename the images files (example: `IMG_123.JPEG -> 1.JPEG`) and convert it with imagemagick to reduce his size.

A second program read the CSV file and make a `.tex` file to compile with LaTeX.

A `makefile` is present to help through the different steps. It also define some variables.

## Dependencies

The workflow is run on GNU/Linus OS.

Scala 2.13.8 (via sdkman): 

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install scala 2.13.8
```

Other dependencies (on Debian/Ubuntu based) :
```bash
sudo apt install texlive texlive-latex-extra texlive-lang-french make imagemagick sed
```
