BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "drzava" (
	"id"	INTEGER,
	"naziv"	TEXT,
	"glavni_grad"	INTEGER,
	PRIMARY KEY("id"),
	FOREIGN KEY("glavni_grad") REFERENCES "grad"("id")
);
CREATE TABLE IF NOT EXISTS "grad" (
	"id"	INTEGER,
	"naziv"	TEXT,
	"broj_stanovnika"	INTEGER,
	"drzava"	INTEGER,
	PRIMARY KEY("id"),
	FOREIGN KEY("drzava") REFERENCES "drzava"("id")
);
INSERT INTO "drzava" VALUES (1,'Francuska',1);
INSERT INTO "drzava" VALUES (2,'Velika Britanija',2);
INSERT INTO "drzava" VALUES (3,'Austrija',3);
INSERT INTO "grad" VALUES (1,'Pariz',2141000,1);
INSERT INTO "grad" VALUES (2,'London',8908000,2);
INSERT INTO "grad" VALUES (3,'Beč',1889000,3);
INSERT INTO "grad" VALUES (4,'Manchester',510746,2);
INSERT INTO "grad" VALUES (5,'Graz',439236,3);
COMMIT;
