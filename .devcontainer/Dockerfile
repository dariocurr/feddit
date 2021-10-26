ARG JAVA_VERSION
FROM mcr.microsoft.com/vscode/devcontainers/java:${JAVA_VERSION}

USER root

ARG INSTALL_MAVEN
ARG MAVEN_VERSION
ARG USER
RUN if [ "${INSTALL_MAVEN}" = "true" ]; then su ${USER} -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""; fi

RUN apt update
RUN apt upgrade -y

USER ${USER}
