<?php
// Delete: حذف مخاطب
// POST /delete.php   بدنه: {"id": 1}

require __DIR__ . '/config.php';

requireMethod('POST');

$id = (int) (readJsonBody()['id'] ?? 0);

$stmt = db()->prepare('DELETE FROM contacts WHERE id = ?');
$stmt->execute([$id]);

if ($stmt->rowCount() === 0) {
    respond(false, 'مخاطب پیدا نشد', null, 404);
}

respond(true, 'مخاطب حذف شد');
