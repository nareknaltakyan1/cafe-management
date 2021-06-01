CREATE TABLE USER
(
    ID                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    USERNAME               NVARCHAR(255) UNIQUE NOT NULL,
    PASSWORD               NVARCHAR(255)        NOT NULL,
    ROLE                   INT                  NOT NULL,
    ENABLED                BIT                  NOT NULL,
    CREATION_DATE           DATETIME             NOT NULL,
    CREATED_BY             NVARCHAR(255)        NOT NULL,
    LAST_MODIFICATION_DATE DATETIME             NOT NULL,
    LAST_MODIFIED_BY       NVARCHAR(255)        NOT NULL,

    INDEX (USERNAME),

    CONSTRAINT fk_USER_ROLE FOREIGN KEY (ROLE)
        REFERENCES RF_ROLE (ID)
)
    engine = InnoDB;
