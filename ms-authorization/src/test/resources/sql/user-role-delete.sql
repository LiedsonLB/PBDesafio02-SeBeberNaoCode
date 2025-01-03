DELETE FROM tb_user_role WHERE user_id NOT IN (
    SELECT id
    FROM tb_users
    WHERE email = 'admin@mail.com'
);

DELETE FROM tb_users WHERE email != 'admin@mail.com';