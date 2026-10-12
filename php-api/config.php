<?php
// تنظیمات مشترک همه‌ی فایل‌های API: اتصال به دیتابیس و توابع کمکی

// اطلاعات اتصال به MySQL (مقادیر پیش‌فرض XAMPP)
const DB_HOST = '127.0.0.1';
const DB_NAME = 'contacts_db';
const DB_USER = 'root';
const DB_PASS = '';

// همه‌ی جواب‌های این API از نوع JSON هستند
header('Content-Type: application/json; charset=utf-8');

// هر خطای پیش‌بینی‌نشده (مثلاً قطع بودن MySQL) به‌جای صفحه‌ی خطای HTML، یک JSON برمی‌گرداند
set_exception_handler(function (Throwable $e) {
    respond(false, 'خطای سرور: ' . $e->getMessage(), null, 500);
});

// اتصال به دیتابیس با PDO
function db(): PDO
{
    static $pdo = null;
    if ($pdo === null) {
        $pdo = new PDO(
            'mysql:host=' . DB_HOST . ';dbname=' . DB_NAME . ';charset=utf8mb4',
            DB_USER,
            DB_PASS,
            [
                PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION, // در صورت خطا Exception پرتاب شود
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,      // نتیجه به شکل آرایه‌ی کلید-مقدار
                PDO::ATTR_EMULATE_PREPARES   => false,                 // عددها به‌صورت عدد برگردند، نه رشته
            ]
        );
    }
    return $pdo;
}

// فرمت یکسان برای همه‌ی جواب‌ها: {"success": ..., "message": ..., "data": ...}
function respond(bool $success, string $message, $data = null, int $status = 200): void
{
    http_response_code($status);
    echo json_encode(
        ['success' => $success, 'message' => $message, 'data' => $data],
        JSON_UNESCAPED_UNICODE
    );
    exit;
}

// اگر متد درخواست (GET یا POST) درست نبود، خطا بده
function requireMethod(string $method): void
{
    if ($_SERVER['REQUEST_METHOD'] !== $method) {
        respond(false, "این آدرس فقط با متد $method کار می‌کند", null, 405);
    }
}

// خواندن بدنه‌ی JSON که Retrofit می‌فرستد
function readJsonBody(): array
{
    $body = json_decode(file_get_contents('php://input'), true);
    return is_array($body) ? $body : [];
}

// گرفتن و بررسی فیلدهای مخاطب از بدنه‌ی درخواست
function readContactFields(array $body): array
{
    $name  = trim((string) ($body['name'] ?? ''));
    $phone = trim((string) ($body['phone'] ?? ''));
    $email = trim((string) ($body['email'] ?? ''));

    if ($name === '' || $phone === '') {
        respond(false, 'نام و شماره تلفن الزامی است', null, 422);
    }

    return [$name, $phone, $email];
}

// خواندن یک مخاطب با شناسه
function findContact(int $id): ?array
{
    $stmt = db()->prepare('SELECT id, name, phone, email FROM contacts WHERE id = ?');
    $stmt->execute([$id]);
    $contact = $stmt->fetch();
    return $contact ?: null;
}
