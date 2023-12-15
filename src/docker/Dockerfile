FROM eclipse-temurin:17.0.9_9-jre-ubi9-minimal
LABEL maintainer="nagyesta@gmail.com"
EXPOSE 8443:8443
ADD lowkey-vault.jar /lowkey-vault.jar
RUN microdnf install shadow-utils
RUN groupadd -r -g 1000 lowkey \
  && useradd -r -u 1000 -g lowkey -m -d /opt/lowkey -s /bin/bash lowkey
RUN chown -R lowkey:lowkey "/lowkey-vault.jar"
RUN chmod 555 "/lowkey-vault.jar"
RUN mkdir "/import"
RUN chown -R lowkey:lowkey "/import"
RUN chmod 755 "/import"
RUN mkdir "/config"
RUN chown -R lowkey:lowkey "/config"
RUN chmod 555 "/config"
USER lowkey
WORKDIR /
CMD [ "sh", "-c", "ls /" ]
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /lowkey-vault.jar ${LOWKEY_ARGS}"]
