FROM ubuntu:22.04

USER root
WORKDIR /live
COPY ./ /live

ENV DEBIAN_FRONTEND=noninteractive

RUN ls /live \
    && bash /live/docker/install.sh

EXPOSE 8080

ENTRYPOINT ["/live/icpc-live-v2/docker/docker_entry.sh"]
CMD ["primary"]
