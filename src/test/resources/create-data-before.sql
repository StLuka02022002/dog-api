DELETE FROM images;
ALTER TABLE images ALTER COLUMN id RESTART WITH 1;

DELETE FROM users;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;

INSERT INTO users(username)
VALUES('username');

INSERT INTO images(image_url, breed, user_id)
VALUES('https://image.com/dog.jpg', 'breed', 1);