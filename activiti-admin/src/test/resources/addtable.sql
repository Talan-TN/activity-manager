CREATE TABLE IF NOT EXISTS TEST_TABLE(
   id INT NOT NULL,
   title VARCHAR(50),
   author VARCHAR(20)
);


insert into TEST_TABLE VALUES(0,'title','author');


commit;