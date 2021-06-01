CREATE TABLE RF_PRODUCT_IN_ORDER_STATUS
(
    ID     INT PRIMARY KEY,
    STATUS NVARCHAR(255) NOT NULL
)
    engine = InnoDB;

INSERT INTO RF_PRODUCT_IN_ORDER_STATUS (ID, STATUS)
VALUES (0, 'ACTIVE');
INSERT INTO RF_PRODUCT_IN_ORDER_STATUS (ID, STATUS)
VALUES (1, 'CANCELED');
