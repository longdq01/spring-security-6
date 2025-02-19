-- eazybank.users definition

CREATE TABLE `users` (
                         `id` varchar(100) NOT NULL,
                         `username` varchar(100) NOT NULL,
                         `password` varchar(100) NOT NULL,
                         `roles` varchar(100) DEFAULT NULL,
                         `email` varchar(100) NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `users_unique_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE eazybank.authorities (
                                      id INT auto_increment NOT NULL,
                                      user_id varchar(100) NOT NULL,
                                      name varchar(100) NOT NULL,
                                      CONSTRAINT authorities_pk PRIMARY KEY (id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
