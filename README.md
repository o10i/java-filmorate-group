Привет! В своей диаграмме я создала соединительную таблицу likes между movie и users, чтобы отразить отдельную логику и 
хранение лайков, а также чтобы упрастить вывод всех фильмов. Такая же ситуация между таблицами users и friends. 
Примеры с SQL запросами ниже.

Диаграмма лежит в папке p2p.


SQL запросы:

-- Получение всех пользователей/фильмов

SELECT *
FROM users;

SELECT *
FROM movie;

-- Получение пользователя/фильма по id

SELECT *
FROM users
WHERE user_id = N;

SELECT *
FROM movie
WHERE film_id = N;


-- Топ N фильмов

SELECT name
FROM movie AS m
LEFT OUTER JOIN likes AS l ON m.film_id = l.film_id
GROUP BY name
ORDER BY COUNT(l.user_id) DESC
LIMIT N;

-- Список друзей пользователя

SELECT *
FROM users AS us
RIGHT OUTER JOIN friends AS f ON us.user_id = f.friend_id_one
LEFT OUTER JOIN users AS u ON f.friend_id_two = u.user_id
WHERE f.friend_id_one = N
AND status = 'confirmed';


-- Список общих друзей

SELECT *
FROM users AS us
RIGHT OUTER JOIN friends AS f ON us.user_id = f.friend_id_one
LEFT OUTER JOIN users AS u ON f.friend_id_two = u.user_id
WHERE f.friend_id_one = N IN ( 	SELECT *
                                FROM users AS us
                                RIGHT OUTER JOIN friends AS f ON us.user_id = f.friend_id_one
                                LEFT OUTER JOIN users AS u ON f.friend_id_two = u.user_id
                                f.friend_id_one = L
                                AND status = 'confirmed')
	AND status = 'confirmed'
GROUP BY f.friend_id_two;
