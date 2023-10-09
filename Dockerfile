FROM openjdk:17

LABEL authors="sebastian"

ADD target/TicketBot-beta-1.2.0.jar ticket.jar
ENTRYPOINT ["java", "-jar","ticket.jar"]
EXPOSE 6066

RUN ["java", "-jar","ticket.jar"]