DROP SCHEMA IF EXISTS superheroSightingsDB;
CREATE SCHEMA superheroSightingsDB;

USE superheroSightingsDB;

CREATE TABLE locations (
	locationId INT AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(500) NOT NULL,
    address VARCHAR(100) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    CONSTRAINT PK_locations PRIMARY KEY (locationId)
);

CREATE TABLE supers (
	superId INT AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(500) NOT NULL,
    isHero BOOLEAN NOT NULL,
    superpower VARCHAR(50) NOT NULL,
    CONSTRAINT PK_supers PRIMARY KEY (superId)
);

CREATE TABLE organizations (
	organizationId INT AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(500) NOT NULL,
    address VARCHAR(100) NOT NULL,
    contact VARCHAR(100) NOT NULL,
    CONSTRAINT PK_organizations PRIMARY KEY (organizationId)
);

CREATE TABLE sightings (
	superId INT NOT NULL,
    locationId INT NOT NULL,
    dateOfSighting DATE NOT NULL,
	CONSTRAINT PK_sightings PRIMARY KEY (superId, locationId, dateOfSighting),
    CONSTRAINT FK_sightings_supers FOREIGN KEY (superId)
		REFERENCES supers (superId),
	CONSTRAINT FK_sightings_locations FOREIGN KEY (locationId)
		REFERENCES locations (locationId)
);

CREATE TABLE organizationHasMembers (
	organizationId INT NOT NULL,
    superId INT NOT NULL,
    CONSTRAINT PK_organizationHasMembers PRIMARY KEY (organizationId, superId),
    CONSTRAINT FK_organizationHasMembers_organizations FOREIGN KEY (organizationId)
		REFERENCES organizations (organizationId),
	CONSTRAINT FK_organizationHasMembers_supers FOREIGN KEY (superId)
		REFERENCES supers (superId)
);