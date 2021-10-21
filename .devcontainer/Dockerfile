ARG JAVA_VERSION
FROM mcr.microsoft.com/vscode/devcontainers/java:${JAVA_VERSION}

USER root

ARG INSTALL_MAVEN
ARG MAVEN_VERSION
RUN if [ "${INSTALL_MAVEN}" = "true" ]; then su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""; fi

RUN git config --global user.email "dariocurr@gmail.com",
RUN git config --global user.name "Dario Curreri",

USER vscode