FROM eclipse-temurin:17.0.14_7-jre-ubi9-minimal
LABEL maintainer="nagyesta@gmail.com"
EXPOSE 8443:8443
COPY lowkey-vault.jar /lowkey-vault.jar
RUN \
  microdnf install shadow-utils && \
  microdnf clean all && \
  groupadd -r -g 1000 lowkey && \
  useradd -r -u 1000 -g lowkey -m -d /opt/lowkey -s /bin/bash lowkey && \
  chown -R lowkey:lowkey "/lowkey-vault.jar" && \
  chmod 555 "/lowkey-vault.jar" && \
  mkdir "/import" && \
  chown -R lowkey:lowkey "/import" && \
  chmod 755 "/import" && \
  mkdir "/config" && \
  chown -R lowkey:lowkey "/config" && \
  chmod 555 "/config"
USER lowkey
WORKDIR /
CMD [ "sh", "-c", "ls /" ]
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /lowkey-vault.jar ${LOWKEY_ARGS}"]
