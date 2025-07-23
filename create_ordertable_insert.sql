-- 确保表不存在时创建
DROP TABLE IF EXISTS t_order;

CREATE TABLE t_order (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
                         user_id BIGINT NOT NULL COMMENT '用户ID',
                         voucher_id BIGINT COMMENT '优惠券ID',
                         pay_type INT COMMENT '支付方式(1:支付宝,2:微信,3:其他)',
                         status INT NOT NULL COMMENT '订单状态(0:待支付,1:已支付,2:已使用,3:已退款,4:已取消)',
                         create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         pay_time DATETIME COMMENT '支付时间',
                         use_time DATETIME COMMENT '使用时间',
                         refund_time DATETIME COMMENT '退款时间',
                         update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         INDEX idx_user_id (user_id),
                         INDEX idx_voucher_id (voucher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 设置自增起始值
ALTER TABLE t_order AUTO_INCREMENT=1;
-- 添加外键约束

-- 插入数据(移除了手动指定的id)
INSERT INTO `t_order` (`user_id`, `voucher_id`, `pay_type`, `status`, `create_time`, `pay_time`, `use_time`, `refund_time`) VALUES
                    (1011, 157, 1, 1, '2025-05-09 13:00:00', NULL, NULL, NULL),
                    (1012, 158, 2, 1, '2025-05-09 14:00:00', '2025-05-09 14:05:00', NULL, NULL),
                    (1013, 123, 1, 2, '2025-05-09 15:00:00', '2025-05-09 15:05:00', NULL, NULL),
                    (1014, 159, 3, 3, '2025-05-09 16:00:00', '2025-05-09 16:05:00', '2025-05-09 16:30:00', NULL),
                    (1015, 160, 1, 4, '2025-05-09 17:00:00', '2025-05-09 17:05:00', '2025-05-09 17:30:00', '2025-05-09 18:00:00'),
                    (1016, 152, 2, 1, '2025-05-10 09:00:00', NULL, NULL, NULL),
                    (1017, 161, 1, 2, '2025-05-10 10:00:00', '2025-05-10 10:05:00', NULL, NULL),
                    (1018, 162, 3, 3, '2025-05-10 11:00:00', '2025-05-10 11:05:00', '2025-05-10 11:30:00', NULL),
                    (1019, 697, 2, 1, '2025-05-10 12:00:00', NULL, NULL, NULL),
                    (1020, 163, 1, 4, '2025-05-10 13:00:00', '2025-05-10 13:05:00', '2025-05-10 13:30:00', '2025-05-10 14:00:00');