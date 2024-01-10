FROM oraclelinux:9-slim

RUN groupadd -r vialink && useradd -g vialink vialink
WORKDIR /var/app
RUN chown -R vialink:vialink .
USER vialink

COPY --chown=vialink:vialink ../target/awt-native-test awt-native-test
COPY --chown=vialink:vialink ../target/*.so ./

ENTRYPOINT ["/var/app/awt-native-test"]