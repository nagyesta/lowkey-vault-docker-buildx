FROM eclipse-temurin:21-jre-jammy
LABEL maintainer="nagyesta@gmail.com"
EXPOSE 8443:8443
ADD lowkey-vault.jar /lowkey-vault.jar
RUN addgroup --system lowkey && adduser --system lowkey --ingroup lowkey
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
