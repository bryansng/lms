CREATE TABLE artifacts (
	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	isbn VARCHAR(13) UNIQUE,
	type VARCHAR(255),
	/* (cd, dvds, pdfs) */
	genre TEXT,
	pdf BLOB,
	-- authors ?? (author_id? how to more than 1 authors)
	authors VARCHAR(255),
	/* (i.e. Bryan Sng, Emily Liew, Braddy Yeoh) */
	title VARCHAR(255),
		/* (during search, search by title, sort by average ratings + number of ratings) */
	original_title VARCHAR(255),
		/* (internal use for grouping books of the different editions, i.e. languages, hardcover, softcover (aka paperbacks), types, formats (cds, dvds, pdfs...)) */
	subtitle TEXT,
	description TEXT,
	publishers VARCHAR(255),
	published_on DATETIME,
	created_on DATETIME DEFAULT CURRENT_TIMESTAMP,
	item_price DECIMAL(19,4) DEFAULT 10.00,
	/* (for restocking from lost) */
	quantity INT,
	rack_location VARCHAR(255)
);

CREATE TABLE members (
	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	email VARCHAR(255) UNIQUE NOT NULL,
	full_name VARCHAR(255),
	gender VARCHAR(255),
	mobile_number VARCHAR(20),
	address TEXT,
	born_on DATETIME,
	joined_on DATETIME DEFAULT CURRENT_TIMESTAMP,
	last_active_on DATETIME,
	bio TEXT,
	type VARCHAR(255) DEFAULT 'member'
);

CREATE TABLE login (
	email VARCHAR(255) NOT NULL PRIMARY KEY,
	hash VARCHAR(100) NOT NULL,
	FOREIGN KEY (email) REFERENCES members(email)
);

CREATE TABLE reserve_queue (
	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	isbn VARCHAR(13),
	member_id INT UNSIGNED,
	/* (add trigger, if artifact is returned, check if waiting list has a user waiting for this artifact, if so, pick the first user waiting for it.) */
	expired_on DATETIME,
	FOREIGN KEY (isbn) REFERENCES artifacts(isbn),
	FOREIGN KEY (member_id) REFERENCES members(id)
);

CREATE TABLE loan_history (
	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	isbn VARCHAR(13),
	member_id INT UNSIGNED,
	issued_on DATETIME DEFAULT CURRENT_TIMESTAMP,
	return_on DATETIME,
	returned_on DATETIME,
	fined_on DATETIME,
	lost_on DATETIME,
	was_lost BOOLEAN DEFAULT FALSE,
	fine DECIMAL(19,4) DEFAULT 0.0,
	status VARCHAR(255),
	FOREIGN KEY (isbn) REFERENCES artifacts(isbn),
	FOREIGN KEY (member_id) REFERENCES members(id)
);

INSERT INTO artifacts(isbn, type, genre, authors, title, original_title, subtitle, description, publishers, published_on, item_price, quantity, rack_location) VALUES
	('9780743269513', 'book', 'Self-Help', 'Stephen R. Covey', 'The 7 Habits of Highly Effective People', 'The 7 Habits of Highly Effective People', 'Powerful Lessons in Personal Change', 'In The 7 Habits of Highly Effective People, author Stephen R. Covey presents a holistic, integrated, principle-centered approach for solving personal and professional problems. With penetrating insights and pointed anecdotes, Covey reveals a step-by-step pathway for living with fairness, integrity, service, and human dignity—principles that give us the security to adapt to change and the wisdom and power to take advantage of the opportunities that change creates.', 'Free Press', '2004-11-09', 10.00, 2, '420'),
	('9780751532715', 'book', 'Personal Finance', 'Robert T. Kiyosaki, Sharon L. Lechter', 'Rich Dad, Poor Dad', 'Rich Dad, Poor Dad', 'What the Rich Teach Their Kids about Money - that the Poor and Middle Class Do Not!', 'Taking to heart the message that the poor and middle class work for money, but the rich have money work for them, the authors lay out a financial philosophy based on the principle that income-generating assets always provide healthier bottom-line results.', 'Time Warner Books UK', PARSEDATETIME('2000','yyyy','en'), 10.00, 1, '69'),
	('9780671723651', 'book', 'Business & Economics', 'Dale Carnegie', 'How To Win Friends And Influence People', 'How To Win Friends And Influence People', '', 'You can go after the job you want...and get it! You can take the job you have...and improve it! You can take any situation you''re in...and make it work for you! For over 50 years the rock-solid, time-tested advice in this book has carried thousands of now famous people up the ladder of success in their business and personal lives. Now this phenomenal book has been revised and updated to help readers achieve their maximum potential in the complex and competitive 90s! Learn: The six ways to make people like you The twelve ways to win people to your way of thinking The nine ways to change people without arousing resentment and much, much more!', 'Simon and Schuster', PARSEDATETIME('1982','yyyy','en'), 10.00, 2, '6954'),
	('9780374275631', 'book', 'Business & Economics', 'Daniel Kahneman', 'Thinking, Fast and Slow', 'Thinking, Fast and Slow', '', 'A psychologist draws on years of research to introduce his "machinery of the mind" model on human decision making to reveal the faults and capabilities of intuitive versus logical thinking.', 'Macmillan', '2011-10-25', 10.00, 2, '7749'),
	('9780061241895', 'book', 'Business & Economics', 'Robert B. Cialdini', 'Influence', 'Influence', 'The Psychology of Persuasion', 'Dr Robert Cialdini explains the six psychological principles that drive the human impulse to comply to the pressures of others and reveals how to defend oneself against manipulation.', 'ICON Group International', PARSEDATETIME('2007','yyyy','en'), 10.00, 2, '9981'),
	('9780307353139', 'book', 'Business & Economics', 'Timothy Ferriss', 'The 4-Hour Work Week', 'The 4-Hour Work Week', 'Escape 9-5, Live Anywhere, and Join the New Rich', 'Offers techniques and strategies for increasing income while cutting work time in half, and includes advice for leading a more fulfilling life.', 'Crown Business', PARSEDATETIME('2007','yyyy','en'), 10.00, 1, '339');

INSERT INTO members(full_name, email, mobile_number, address, type) VALUES
	('Bryan Sng', 'hong.sng@ucdconnect.ie', '17205050', 'Mountbrook', 'librarian'),
	('James Bond', 'bond.james@ucdconnect.ie', '007', 'MI6', 'member'),
	('Obi Wan', 'wan.obi@ucdconnect.ie', '69', 'The High Ground', 'member'),
	('Steve Jobs', 'jobs.steve@ucdconnect.ie', '1234', 'Silicon Valley', 'member');

INSERT INTO login(email, hash) VALUES
	('hong.sng@ucdconnect.ie', 'root'),
	('bond.james@ucdconnect.ie', 'root'),
	('wan.obi@ucdconnect.ie', 'root'),
	('jobs.steve@ucdconnect.ie', 'root');

INSERT INTO loan_history(isbn, member_id, return_on, status) VALUES
	('9780743269513', '1', NOW() + 3, 'issued'),
	('9780751532715', '1', NOW() + 3, 'issued'),
	('9780743269513', '2', NOW() + 3, 'issued'),
	('9780307353139', '2', NOW() + 3, 'issued');

INSERT INTO reserve_queue(isbn, member_id, expired_on) VALUES
	('9780743269513', '3', NOW() + 3),
	('9780307353139', '1', NOW() + 3),
	('9780307353139', '3', NOW() + 3),
	('9780751532715', '2', NOW() + 3),
	('9780751532715', '3', NOW() + 3);
