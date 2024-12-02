INSERT INTO categories (name, description) VALUES ('Fantasy', 'Cool');
INSERT INTO books (title, author, isbn, price, description, cover_image)
VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 10.99, 'A novel about the American dream', 'cover-image-url');

INSERT INTO books_categories (book_id, category_id)
VALUES ((SELECT id FROM books WHERE title = 'The Great Gatsby'), (SELECT id FROM categories WHERE name = 'Fantasy'));
