FROM debian:stable
RUN apt-get update && apt-get install -y texlive texlive-latex-extra texlive-lang-french make imagemagick sed curl wget zip unzip git
RUN curl -s "https://get.sdkman.io" | bash
# this SHELL command is needed to allow using source
SHELL ["/bin/bash", "-c"]
RUN source "/root/.sdkman/bin/sdkman-init.sh" && sdk install java 21.0.8-tem && sdk install scala 3.3.4

WORKDIR /root/trombinoscope

COPY ./in /root/trombinoscope/in
COPY ./photos /root/trombinoscope/photos
COPY ./src /root/trombinoscope/src
COPY ./makefile /root/trombinoscope/

