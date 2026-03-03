-- V5__seed_users.sql
DELETE FROM user_roles WHERE user_id IN
    (SELECT id FROM users WHERE username IN ('admin','user','viewer','ops'));
DELETE FROM users WHERE username IN ('admin','user','viewer','ops');

INSERT INTO users (username, password, email, full_name, enabled, created_at)
VALUES
('admin',  '$2a$12$D5CTepAmJTV8uW93ZNGKoeCSa.B/Ukej.vLBO8Z0OOkgm7.sGiBCO',
 'admin@agilesolutions.com',  'System Administrator', TRUE, NOW()),
('user',   '$2a$12$o3J5IgE8zbHe56tQUEbAPeH.HezR12LeZtXAtk2cs9mNEqRdvpotm',
 'user@agilesolutions.com',   'Standard User',        TRUE, NOW()),
('viewer', '$2a$12$hciyUdhTEFKPDaYMQY7pa.y29aOo1UQ3HMAslomjqtU5qRLMTC0da',
 'viewer@agilesolutions.com', 'Read-Only Viewer',     TRUE, NOW()),
('ops',    '$2a$12$TrJnO3XIpAIdUeM4sAMvc.wt9gplXL4ZJb2IXtML1ZB3fwPSw9mei',
 'ops@agilesolutions.com',    'Operations User',      TRUE, NOW());

INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin';
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_USER'  FROM users WHERE username = 'admin';
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_USER'  FROM users WHERE username = 'user';
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_VIEWER' FROM users WHERE username = 'viewer';
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_USER'  FROM users WHERE username = 'ops';
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_OPS'   FROM users WHERE username = 'ops';