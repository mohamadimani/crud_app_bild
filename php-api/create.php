<?php
// Create: افزودن مخاطب جدید
// POST /create.php   بدنه: {"name": "...", "phone": "...", "email": "..."}

require __DIR__ . '/config.php';

requireMethod('POST');

[$name, $phone, $email] = readContactFields(readJsonBody());

$stmt = db()->prepare('INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)');
$stmt->execute([$name, $phone, $email]);

// رکورد ساخته‌شده را (همراه با id که دیتابیس داده) برمی‌گردانیم تا اپ آن را در Room ذخیره کند
$contact = findContact((int) db()->lastInsertId());

respond(true, 'مخاطب اضافه شد', $contact, 201);
