CREATE TABLE notes (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	versions INT NOT NULL DEFAULT 1,
	created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	title VARCHAR(255) NOT NULL,
	note TEXT
);

INSERT INTO notes(title, note) VALUES
	('by obi wan', 'hello there'),
	('also by obi wan', 'its over anakin, I have the high ground.');
