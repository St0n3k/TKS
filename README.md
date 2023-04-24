# TKS

Project for *Technologies of Network Components* at Lodz University of Technology.

![Visitors](https://api.visitorbadge.io/api/visitors?path=https%3A%2F%2Fgithub.com%2FSt0n3k&countColor=%23697689)

## :construction_worker: Contributors

| Name   | Github                                            |
| ------ | ------------------------------------------------- |
| Rafał  | [rstrzalkowski](https://github.com/rstrzalkowski) |
| Łukasz | [Lukasz0104](https://github.com/Lukasz0104)       |
| Kamil  | [St0n3k](https://github.com/St0n3k)               |

## Technologies, languages and tools

- Java (JDK-17)
- Maven
- Lombok
- Jakarta EE
- Payara Server
- SOAP
- JWT
- EclipseLink
- PostgreSQL
- Rest-assured
- Testcontainers
- Arquillian
- RabbitMQ
- Docker
- Microprofile:
  - Config
  - Health
  - Metrics

## :clipboard: Description

[Guesthouse](https://github.com/St0n3k/PAS) application rebuilt following DDD patern and using hexagonal architecture. Further splitted into microservices.

## Modules

### :office_worker: UserService

This project contains logic for managing all types of accounts. It is also responsible for authentication, as well as it sends messages to [RentService](#rent) via a message broker (here RabbitMQ was used) when new client was registered.

### :hotel: RentService {#rent}

This project contains all of the remaining bussiness logic: it manages rooms, reservations and contains partial data about clients.
