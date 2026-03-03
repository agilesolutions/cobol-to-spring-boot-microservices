-- V5__seed_users.sql
DELETE FROM user_roles WHERE user_id IN
    (SELECT id FROM users WHERE username IN ('admin','user','viewer','ops'));
DELETE FROM users WHERE username IN ('admin','user','viewer','ops');

INSERT INTO users (username, password, email, full_name, enabled, created_at)
VALUES
('admin',  '$2a$12$1S3GhSMVMjrRMQPbJ9c2IOF.Gy4CRF.KK7ixQO7v2sYbWqBtFNE2.',
 'admin@agilesolutions.com',  'System Administrator', TRUE, NOW()),
('user',   '$2a$12$7Jx0wLkHl9Pby2eJHGp4cO9SWZ.zFEYP2XpQs1mXLuGDv6Rj3Nkme',
 'user@agilesolutions.com',   'Standard User',        TRUE, NOW()),
('viewer', '$2a$12$KHl4xQs1mXL3GhSMVMjrRMuGDv6Rj3Nkme7Jx0wLkH9Pby2eJHGp4',
 'viewer@agilesolutions.com', 'Read-Only Viewer',     TRUE, NOW()),
('ops',    '$2a$12$9Pby2eJHGp4cO9SWZ.zFEYP7Jx0wLkHl2XpQs1mXLuGDv6Rj3Nkme',
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