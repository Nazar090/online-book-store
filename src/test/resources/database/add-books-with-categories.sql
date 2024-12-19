INSERT INTO categories (name, description) VALUES
    ('Fantasy', 'Fantasy books description'),
    ('Horror', 'Horror books description'),
    ('Science Fiction', 'Sci-fi books description');

INSERT INTO books (title, author, isbn, price, description, cover_image) VALUES
    ('Book 1', 'Author 1', '12345', 20.00, 'Fantasy Book Description', 'cover1.jpg'),
    ('Book 2', 'Author 2', '67890', 20.00, 'Horror Book Description', 'cover2.jpg');

INSERT INTO books_categories (book_id, category_id) VALUES
    (1, 1),
    (2, 3);
