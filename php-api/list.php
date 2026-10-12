<?php
// Read: لیست همه‌ی مخاطبین
// GET /list.php

require __DIR__ . '/config.php';

requireMethod('GET');

$contacts = db()
    ->query('SELECT id, name, phone, email FROM contacts ORDER BY name')
    ->fetchAll();

respond(true, 'لیست مخاطبین', $contacts);
