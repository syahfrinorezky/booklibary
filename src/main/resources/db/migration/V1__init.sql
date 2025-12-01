CREATE TABLE
    members (
        id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
        member_code VARCHAR(20) NOT NULL UNIQUE,
        name VARCHAR(150) NOT NULL,
        email VARCHAR(150) NOT NULL UNIQUE,
        phone VARCHAR(15),
        address VARCHAR(255),
        created_at DATETIME (3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
        updated_at DATETIME (3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
        deleted_at DATETIME (3),
        INDEX idx_member_code (member_code),
        INDEX idx_email (email)
    );

CREATE TABLE
    categories (
        id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
        category_code VARCHAR(20) NOT NULL UNIQUE,
        name VARCHAR(100) NOT NULL UNIQUE,
        description TEXT,
        created_at DATETIME (3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
        updated_at DATETIME (3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
        deleted_at DATETIME (3),
        INDEX idx_category_code (category_code),
        INDEX idx_name (name)
    );

CREATE TABLE
    books (
        id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
        book_code VARCHAR(20) NOT NULL UNIQUE,
        category_id BIGINT UNSIGNED,
        title VARCHAR(200) NOT NULL,
        author VARCHAR(150) NOT NULL,
        isbn VARCHAR(20) NOT NULL UNIQUE,
        publisher VARCHAR(100),
        published_year YEAR,
        description TEXT,
        stock INT NOT NULL DEFAULT 0,
        created_at DATETIME (3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
        updated_at DATETIME (3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
        deleted_at DATETIME (3),
        INDEX idx_book_code (book_code),
        INDEX idx_category_id (category_id),
        INDEX idx_title (title),
        INDEX idx_author (author),
        INDEX idx_isbn (isbn),
        CONSTRAINT fk_books_category FOREIGN KEY (category_id) REFERENCES categories (id)
    );

CREATE TABLE
    borrows (
        id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
        borrow_code VARCHAR(20) NOT NULL UNIQUE,
        member_id BIGINT UNSIGNED NOT NULL,
        book_id BIGINT UNSIGNED NOT NULL,
        borrow_date DATETIME (3) NOT NULL,
        due_date DATETIME (3) NOT NULL,
        return_date DATETIME (3),
        status ENUM ('BORROWED', 'RETURNED', 'LATE', 'LOST') NOT NULL DEFAULT 'BORROWED',
        notes TEXT,
        penalty_fee DECIMAL(10, 2) DEFAULT 0.00,
        created_at DATETIME (3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
        updated_at DATETIME (3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
        deleted_at DATETIME (3),
        INDEX idx_borrow_code (borrow_code),
        INDEX idx_member_id (member_id),
        INDEX idx_book_id (book_id),
        INDEX idx_status (status),
        CONSTRAINT fk_borrows_member FOREIGN KEY (member_id) REFERENCES members (id),
        CONSTRAINT fk_borrows_book FOREIGN KEY (book_id) REFERENCES books (id)
    );