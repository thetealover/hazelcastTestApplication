FROM haproxy:2.7.8
COPY haproxy.cfg /usr/local/etc/haproxy/haproxy.cfg
EXPOSE 80