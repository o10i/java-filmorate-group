DELETE FROM FILM_GENRE;
DELETE FROM LIKES;
DELETE FROM FOLLOW;
DELETE FROM MOVIE;
DELETE FROM USERS;

ALTER TABLE MOVIE ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;

MERGE INTO RATING KEY (ID)
        VALUES ( 1, 'G' ),
               ( 2, 'PG' ),
               ( 3, 'PG13' ),
               ( 4, 'R' ),
               ( 5, 'NC17' );

MERGE INTO GENRE KEY (ID)
    VALUES ( 1, 'Action' ),
           ( 2, 'Comedy' ),
           ( 3, 'Drama' ),
           ( 4, 'Fantasy' ),
           ( 5, 'Horror' ),
           ( 6, 'Mystery' ),
           ( 7, 'Romance' ),
           ( 8, 'Thriller' );








