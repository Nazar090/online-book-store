DELETE FROM books_categories
WHERE book_id = (SELECT id FROM books WHERE title = 'The Great Gatsby')
AND category_id = (SELECT id FROM categories WHERE name = 'Fantasy');
DELETE FROM books WHERE title = 'The Great Gatsby';
DELETE FROM categories WHERE name = 'Fantasy';
