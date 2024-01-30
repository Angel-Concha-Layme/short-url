CREATE TABLE IF NOT EXISTS `users` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `urls` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `complete_url` VARCHAR(2048) NOT NULL,
  `short_url` VARCHAR(7) NOT NULL UNIQUE,
  `name` VARCHAR(255),
  `description` TEXT,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME,
  `user_id` INT,
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

CREATE TABLE IF NOT EXISTS `quick_urls` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `complete_url` VARCHAR(2048) NOT NULL,
  `short_url` VARCHAR(7) NOT NULL UNIQUE
);
