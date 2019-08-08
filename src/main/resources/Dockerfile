FROM registry.cn-hangzhou.aliyuncs.com/joywise-iedu/alpine-tomcat:7.1

RUN rm -rf /opt/tomcat/webapps/*

ADD h5api.jar /h5api.jar

EXPOSE 5099

ENTRYPOINT ["java","-jar","h5api.jar"]