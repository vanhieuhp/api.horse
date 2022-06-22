CREATE TABLE account
(
    id       INT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status   INT          NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

CREATE TABLE horse
(
    id         INT AUTO_INCREMENT NOT NULL,
    foaled     date         NOT NULL,
    name       VARCHAR(255) NOT NULL,
    price      INT NULL,
    trainer_id INT          NOT NULL,
    CONSTRAINT pk_horse PRIMARY KEY (id)
);

CREATE TABLE trainer
(
    id         INT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NOT NULL,
    account_id INT NULL,
    CONSTRAINT pk_trainer PRIMARY KEY (id)
);

ALTER TABLE account
    ADD CONSTRAINT uc_account_username UNIQUE (username);

ALTER TABLE horse
    ADD CONSTRAINT FK_HORSE_ON_TRAINER FOREIGN KEY (trainer_id) REFERENCES trainer (id);

ALTER TABLE trainer
    ADD CONSTRAINT FK_TRAINER_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (id);