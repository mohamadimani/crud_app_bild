<?php
// Update: ویرایش مخاطب
// POST /update.php   بدنه: {"id": 1, "name": "...", "phone": "...", "email": "..."}

require __DIR__ . '/config.php';

requireMethod('POST');

$body = readJsonBody();
$id = (int) ($body['id'] ?? 0);
[$name, $phone, $email] = readContactFields($body);

if (findContact($id) === null) {
    respond(false, 'مخاطب پیدا نشد', null, 404);
}

$stmt = db()->prepare('UPDATE contacts SET name = ?, phone = ?, email = ? WHERE id = ?');
$stmt->execute([$name, $phone, $email, $id]);

respond(true, 'مخاطب ویرایش شد', findContact($id));
