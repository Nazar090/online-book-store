DELETE FROM books_categories;
DELETE FROM categories;

INSERT INTO categories (name, description) VALUES ('Fantasy', 'Cool');

INSERT INTO books (title, author, isbn, price, description, cover_image)
VALUES ('Book 1', 'Author 1', '32345', 10.99, 'Description 1', 'cover-image-url');

INSERT INTO books_categories (book_id, category_id)
VALUES ((SELECT id FROM books WHERE title = 'Book 1'), (SELECT id FROM categories WHERE name = 'Fantasy'));
