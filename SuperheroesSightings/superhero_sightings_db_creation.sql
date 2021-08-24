DROP SCHEMA IF EXISTS superheroSightingsDB;
CREATE SCHEMA superheroSightingsDB;
USE superheroSightingsDB;

CREATE TABLE locations (
	locationId INT AUTO_INCREMENT,
    locationName VARCHAR(50) NOT NULL,
    locationDescription VARCHAR(500) NOT NULL,
    locationAddress VARCHAR(100) NOT NULL,
    locationLatitude FLOAT NOT NULL,
    locationLongitude FLOAT NOT NULL,
    CONSTRAINT PK_location PRIMARY KEY (locationId)
);

CREATE TABLE superpowers (
	superpowerId INT AUTO_INCREMENT,
    superpowerName VARCHAR(50),
    CONSTRAINT PK_superpower PRIMARY KEY (superpowerId)
);

CREATE TABLE supers (
	superId INT AUTO_INCREMENT,
    superName VARCHAR(50) NOT NULL,
    superDescription VARCHAR(500) NOT NULL,
    superIsHero BOOLEAN NOT NULL,
    CONSTRAINT PK_supers PRIMARY KEY (superId)
);

CREATE TABLE supersHaveSuperpowers (
	superId INT NOT NULL,
    superpowerId INT NOT NULL,
    CONSTRAINT PK_supersHaveSuperpowers PRIMARY KEY (superId, superpowerId),
    CONSTRAINT FK_supersHaveSuperpowers_supers FOREIGN KEY (superId)
		REFERENCES supers (superId),
	CONSTRAINT FK_supersHaveSuperpowers_superpowerId FOREIGN KEY (superpowerId)
		REFERENCES superpowers (superpowerId)
);

CREATE TABLE organizations (
	organizationId INT AUTO_INCREMENT,
    organizationName VARCHAR(50) NOT NULL,
    organizationDescription VARCHAR(500) NOT NULL,
    organizationAddress VARCHAR(100) NOT NULL,
    organizationContact VARCHAR(100) NOT NULL,
    CONSTRAINT PK_organizations PRIMARY KEY (organizationId)
);

CREATE TABLE sightings (
	sightingId INT AUTO_INCREMENT,
	superId INT NOT NULL,
    locationId INT NOT NULL,
    dateOfSighting DATE NOT NULL,
	CONSTRAINT PK_sightings PRIMARY KEY (sightingId),
    CONSTRAINT FK_sightings_supers FOREIGN KEY (superId)
		REFERENCES supers (superId),
	CONSTRAINT FK_sightings_locations FOREIGN KEY (locationId)
		REFERENCES locations (locationId)
);

CREATE TABLE organizationsHaveMembers (
	organizationId INT NOT NULL,
    superId INT NOT NULL,
    CONSTRAINT PK_organizationHasMembers PRIMARY KEY (organizationId, superId),
    CONSTRAINT FK_organizationHasMembers_organizations FOREIGN KEY (organizationId)
		REFERENCES organizations (organizationId),
	CONSTRAINT FK_organizationHasMembers_supers FOREIGN KEY (superId)
		REFERENCES supers (superId)
);