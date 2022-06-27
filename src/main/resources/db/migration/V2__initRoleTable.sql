
CREATE TABLE account_role
(
    account_id INT NOT NULL,
    role_id   INT NOT NULL
);

CREATE TABLE role
(
    id     INT AUTO_INCREMENT NOT NULL,
    name   VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

ALTER TABLE account_role
    ADD CONSTRAINT fk_role_on_account FOREIGN KEY (account_id) REFERENCES account (id);

ALTER TABLE account_role
    ADD CONSTRAINT fk_account_on_role FOREIGN KEY (role_id) REFERENCES role (id);