CREATE TABLE "film" ("id" INTEGER NOT NULL,
"name" VARCHAR(255),
"year" INTEGER,
"img" INTEGER,
"rating" REAL,
CONSTRAINT "film_id" PRIMARY KEY("id"));
-- Create index film_name
CREATE INDEX "film_name" ON "film"( "name" );
BEGIN; 
END; 
-- Create index film_year
CREATE INDEX "film_year" ON "film"( "year" );
BEGIN; 
END; 
CREATE TABLE "perforemer" ("id" INTEGER NOT NULL,
"name" VARCHAR(255),
CONSTRAINT "perforemer_id" PRIMARY KEY("id"));
-- Create index perforemer_name
CREATE INDEX "perforemer_name" ON "perforemer"( "name" );
BEGIN; 
END; 
CREATE TABLE [music] ("id" INTEGER NOT NULL,
"name" VARCHAR(45),
"performer_id" INTEGER,
"rating" REAL,
CONSTRAINT "music_id" PRIMARY KEY("id"),
FOREIGN KEY ("performer_id") REFERENCES [perforemer]("id") ON DELETE CASCADE ON UPDATE CASCADE);
-- Create index music_name
CREATE INDEX "music_name" ON "music"( "name" );
BEGIN; 
END; 
-- Create index music_performer_id
CREATE INDEX "music_performer_id" ON "music"( "performer_id" );
BEGIN; 
END; 
CREATE TABLE "film_music" ("film_id" INTEGER NOT NULL,
"music_id" INTEGER NOT NULL,
CONSTRAINT "film_music_film_idmusic_id" PRIMARY KEY("film_id","music_id"));
-- Create index film_music_film_id
CREATE INDEX "film_music_film_id" ON "film_music"( "film_id" );
BEGIN; 
END; 
-- Create index film_music_music_id
CREATE INDEX "film_music_music_id" ON "film_music"( "music_id" );
BEGIN; 
END; 
CREATE TABLE "user" ("id" INTEGER NOT NULL,
"name" VARCHAR(45),
CONSTRAINT "user_id" PRIMARY KEY("id"));
CREATE TABLE [favorites] ("user_id" INTEGER NOT NULL,
"music_id" INTEGER NOT NULL,
"date_time" DATETIME,
CONSTRAINT "favorites_user_idmusic_id" PRIMARY KEY("user_id","music_id"));
CREATE INDEX "favorites_music_id" ON "favorites"( "music_id" );
BEGIN; 
END; 
-- Create index favorites_user_id
CREATE INDEX "favorites_user_id" ON "favorites"( "user_id" );
BEGIN; 
END; 
CREATE TABLE "music_rating" ("music_id" INTEGER NOT NULL,
"user_id" INTEGER NOT NULL,
"value" INTEGER,
"date_time" DATETIME,
CONSTRAINT "music_rating_music_iduser_id" PRIMARY KEY("music_id","user_id"));
-- Create index music_rating_music_id
CREATE INDEX "music_rating_music_id" ON "music_rating"( "music_id" );
BEGIN; 
END; 
-- Create index music_rating_user_id
CREATE INDEX "music_rating_user_id" ON "music_rating"( "user_id" );
BEGIN; 
END; 
