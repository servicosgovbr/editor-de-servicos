FROM java:openjdk-8u72-jdk


ADD ./build/distributions /opt/portal-de-servicos
WORKDIR /opt/editor-de-servicos


RUN tar xvf editor-de-servicos-1.0.0.tar

WORKDIR /opt/editor-de-servicos/editor-de-servicos-1.0.0/bin

EXPOSE 8090
CMD ./editor-de-servicos