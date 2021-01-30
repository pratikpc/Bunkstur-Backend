# Note: You can use any Debian/Ubuntu based image you want. 
FROM mcr.microsoft.com/vscode/devcontainers/base:0-buster

# [Option] Install zsh
ARG INSTALL_ZSH="true"
# [Option] Upgrade OS packages to their latest versions
ARG UPGRADE_PACKAGES="false"
# [Option] Enable non-root Docker access in container
ARG ENABLE_NONROOT_DOCKER="true"
# [Option] Use the OSS Moby Engine instead of the licensed Docker Engine
ARG USE_MOBY="true"

# Install needed packages and setup non-root user. Use a separate RUN statement to add your
# own dependencies. A user of "automatic" attempts to reuse an user ID if one already exists.
ARG USERNAME=automatic
ARG USER_UID=1000
ARG USER_GID=$USER_UID
COPY library-scripts/*.sh /tmp/library-scripts/
RUN apt-get update \
    && /bin/bash /tmp/library-scripts/common-debian.sh "${INSTALL_ZSH}" "${USERNAME}" "${USER_UID}" "${USER_GID}" "${UPGRADE_PACKAGES}" \
    # Use Docker script from script library to set things up
    && /bin/bash /tmp/library-scripts/docker-in-docker-debian.sh "${ENABLE_NONROOT_DOCKER}" "${USERNAME}" "${USE_MOBY}" \
    # Clean up
    && apt-get autoremove -y && apt-get clean -y && rm -rf /var/lib/apt/lists/* /tmp/library-scripts/

VOLUME [ "/var/lib/docker" ]

# Setting the ENTRYPOINT to docker-init.sh will start up the Docker Engine 
# inside the container "overrideCommand": false is set in devcontainer.json. 
# The script will also execute CMD if you need to alter startup behaviors.
ENTRYPOINT [ "/usr/local/share/docker-init.sh" ]
CMD [ "sleep", "infinity" ]

# [Optional] Uncomment this section to install additional OS packages.
# RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
#     && apt-get -y install --no-install-recommends <your-package-list-here>

# Maven
ARG MAVEN_VERSION=3.6.3
RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
    && curl -fsSL -o /tmp/apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
    && rm -f /tmp/apache-maven.tar.gz \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
# Gradle
ENV GRADLE_HOME /opt/gradle
ARG GRADLE_VERSION=6.8.1
RUN set -o errexit -o nounset \
    && echo "Downloading Gradle" \
    && wget \
    --no-verbose \
    --output-document=gradle.zip "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
    && echo "Installing Gradle" \
    && unzip gradle.zip \
    && rm gradle.zip \
    && mv "gradle-${GRADLE_VERSION}" "${GRADLE_HOME}/" \
    && ln --symbolic "${GRADLE_HOME}/bin/gradle" /usr/bin/gradle 
    
# AWS Corretto
ENV JAVA_HOME /usr/local/openjdk-11
ENV PATH $PATH:$JAVA_HOME/bin
ARG JAVA_VERSION=11.0.10.9-1
RUN export DEBIAN_FRONTEND=noninteractive \
    && set -eux \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
    curl ca-certificates gnupg software-properties-common fontconfig java-common python3 python3-pip python3-setuptools \
    && curl -fL https://apt.corretto.aws/corretto.key | apt-key add - \
    && add-apt-repository 'deb https://apt.corretto.aws stable main' \
    && mkdir -p /usr/share/man/man1 || true \
    && apt-get update \
    && apt-get install -y java-11-amazon-corretto-jdk=1:$JAVA_VERSION \
    && apt-get purge -y --auto-remove -o APT::AutoRemove::RecommendsImportant=false \
    software-properties-common

ENV LANG C.UTF-8
ENV JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto

# Add AWS CLIs
# Install additional OS packages.
RUN cd /opt && \
    curl https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip -o awscliv2.zip && \
    unzip awscliv2.zip && \
    ./aws/install && \
    rm -rf ./awscliv2.zip \
    && echo "Testing AWS installation" \
    && aws --version

RUN mkdir -p /opt/heroku \
    && curl -fsSL -o /tmp/heroku.tar.gz https://cli-assets.heroku.com/heroku-linux-x64.tar.gz \
    && tar -xzf /tmp/heroku.tar.gz -C /opt/heroku --strip-components=1 \
    && rm -f /tmp/heroku.tar.gz \
    && ln -s /opt/heroku/bin/heroku /usr/bin/heroku
    
# Use Nano as editor
RUN git config --global core.editor "nano" \
## Disable CRLF conversion
    && git config --global core.autocrlf false \
## Set LF as default
    &&  git config --global core.eol lf
