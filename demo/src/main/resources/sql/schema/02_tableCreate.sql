CREATE TABLE IF NOT EXISTS hero (
    Id SERIAL,
    Name VARCHAR(100),
    Class VARCHAR(100),
    Level INT,

    PRIMARY KEY (Id)
);

CREATE TABLE IF NOT EXISTS apprentice (
    Id SERIAL,
    Name VARCHAR(100),

    PRIMARY KEY (Id)
);


CREATE TABLE IF NOT EXISTS skill (
    Id SERIAL,
    Name VARCHAR(100),
    Description VARCHAR(255),

    PRIMARY KEY (Id)
);