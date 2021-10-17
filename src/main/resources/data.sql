INSERT INTO `users` VALUES
(1, '2021-01-01', '2006-07-09', 'admin@admin.admin', 'admin', 'admin', '$2a$10$ANPR9MXIkQqppTz2Ps.BpuiqPQixwJ3FYCM54LThfN69tuTs7lq3S', 'admin');

INSERT INTO `roles` VALUES
(1, '2020-01-01', 'ADMIN'),
(2, '2020-01-01', 'USER');

INSERT INTO `users_roles` VALUES
(1, 1),
(1, 2);

