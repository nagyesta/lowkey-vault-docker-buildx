FROM eclipse-temurin:17.0.15_6-jre-ubi9-minimal
LABEL maintainer="nagyesta@gmail.com"
EXPOSE 8443:8443
COPY lowkey-vault.jar /lowkey-vault.jar
RUN \
  chmod 555 "/lowkey-vault.jar" && \
  mkdir "/import" && \
  chmod 755 "/import" && \
  mkdir "/config" && \
  chmod 555 "/config"
WORKDIR /
CMD [ "sh", "-c", "ls /" ]
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /lowkey-vault.jar ${LOWKEY_ARGS}"]
