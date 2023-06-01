CREATE TABLE `scores` (
    `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `account_number` VARCHAR NOT NULL,
    `score` INTEGER,
    `timestamp` TIMESTAMP
);