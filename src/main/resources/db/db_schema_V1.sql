-- ==========================================
-- PostgreSQL Schema & Init Data cho R2S LeadOps
-- ==========================================

-- 1. Bảng roles
CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE,
                       description VARCHAR(255)
);

-- 2. Bảng users
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role_id INT REFERENCES roles(id) ON DELETE RESTRICT,
                       status VARCHAR(20) DEFAULT 'ACTIVE',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Bảng courses
CREATE TABLE courses (
                         id SERIAL PRIMARY KEY,
                         code VARCHAR(50) NOT NULL UNIQUE,
                         name VARCHAR(100) NOT NULL,
                         description TEXT,
                         status VARCHAR(20) DEFAULT 'ACTIVE'
);

-- 4. Bảng lead_statuses
CREATE TABLE lead_statuses (
                               id SERIAL PRIMARY KEY,
                               code VARCHAR(50) NOT NULL UNIQUE,
                               name VARCHAR(100) NOT NULL,
                               order_index INT DEFAULT 0,
                               is_final BOOLEAN DEFAULT FALSE
);

-- 5. Bảng lead_sources
CREATE TABLE lead_sources (
                              id SERIAL PRIMARY KEY,
                              code VARCHAR(50) NOT NULL UNIQUE,
                              name VARCHAR(100) NOT NULL,
                              description TEXT
);

-- 6. Bảng campaigns
CREATE TABLE campaigns (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           course_id INT REFERENCES courses(id),
                           start_date DATE,
                           end_date DATE,
                           utm_source VARCHAR(100),
                           utm_medium VARCHAR(100),
                           utm_campaign VARCHAR(100),
                           status VARCHAR(20) DEFAULT 'ACTIVE'
);

-- 7. Bảng leads
CREATE TABLE leads (
                       id SERIAL PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       phone VARCHAR(20),
                       normalized_phone VARCHAR(20),
                       email VARCHAR(100),
                       interested_course_id INT REFERENCES courses(id),
                       current_level VARCHAR(50),
                       study_need TEXT,
                       expected_enrollment_time VARCHAR(100),
                       preferred_contact_time VARCHAR(100),
                       school VARCHAR(150),
                       major VARCHAR(100),
                       year_of_study VARCHAR(50),
                       city VARCHAR(100),
                       source_id INT REFERENCES lead_sources(id),
                       campaign_id INT REFERENCES campaigns(id),
                       status_id INT REFERENCES lead_statuses(id),
                       assigned_user_id INT REFERENCES users(id),
                       total_score INT DEFAULT 0,
                       lead_temperature VARCHAR(20),
                       next_follow_up_at TIMESTAMP,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 8. Bảng lead_activities
CREATE TABLE lead_activities (
                                 id SERIAL PRIMARY KEY,
                                 lead_id INT REFERENCES leads(id) ON DELETE CASCADE,
                                 activity_type VARCHAR(50),
                                 content TEXT,
                                 result VARCHAR(100),
                                 old_status_id INT REFERENCES lead_statuses(id),
                                 new_status_id INT REFERENCES lead_statuses(id),
                                 performed_by INT REFERENCES users(id),
                                 performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 next_follow_up_at TIMESTAMP
);

-- 9. Bảng lead_score_events
CREATE TABLE lead_score_events (
                                   id SERIAL PRIMARY KEY,
                                   lead_id INT REFERENCES leads(id) ON DELETE CASCADE,
                                   rule_code VARCHAR(50),
                                   description TEXT,
                                   score_change INT NOT NULL,
                                   score_after INT NOT NULL,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 10. Bảng lead_assignments
CREATE TABLE lead_assignments (
                                  id SERIAL PRIMARY KEY,
                                  lead_id INT REFERENCES leads(id) ON DELETE CASCADE,
                                  assigned_from INT REFERENCES users(id),
                                  assigned_to INT REFERENCES users(id),
                                  assigned_by INT REFERENCES users(id),
                                  reason TEXT,
                                  assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 11. Bảng webhook_events
CREATE TABLE webhook_events (
                                id SERIAL PRIMARY KEY,
                                source VARCHAR(50) NOT NULL,
                                external_event_id VARCHAR(100),
                                payload JSONB,
                                processing_status VARCHAR(50),
                                error_message TEXT,
                                received_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                processed_at TIMESTAMP
);

-- 12. Bảng audit_logs
CREATE TABLE audit_logs (
                            id SERIAL PRIMARY KEY,
                            user_id INT REFERENCES users(id),
                            action VARCHAR(50) NOT NULL,
                            entity_type VARCHAR(50),
                            entity_id INT,
                            old_value JSONB,
                            new_value JSONB,
                            ip_address VARCHAR(50),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- DỮ LIỆU BAN ĐẦU (SEED DATA)
-- ==========================================

-- Seed Roles
INSERT INTO roles (name, description) VALUES
                                          ('ROLE_ADMIN', 'Quản trị viên hệ thống'),
                                          ('ROLE_LEADER_MARKETING', 'Trưởng phòng Marketing'),
                                          ('ROLE_SALES', 'Nhân viên tư vấn (Sales)'),
                                          ('ROLE_MARKETING_STAFF', 'Nhân viên Marketing');

-- Seed User Admin
-- Mật khẩu mặc định là: 123456 (Đã hash bằng BCrypt)
INSERT INTO users (full_name, email, password_hash, role_id, status) VALUES
    ('Admin R2S', 'admin@r2s.edu.vn', '$2a$12$Z0J6Q2QyM5Vf1D9B5K.jL.O/8N5Pz8gQ8H4f7A4f/R1f.E.V/M.uO', 1, 'ACTIVE');

-- Seed Courses
INSERT INTO courses (code, name, description, status) VALUES
                                                          ('JAVA_BACKEND', 'Java Spring Boot', 'Khóa học lập trình Backend với Java Spring Boot', 'ACTIVE'),
                                                          ('REACTJS', 'ReactJS', 'Khóa học lập trình Frontend với ReactJS', 'ACTIVE'),
                                                          ('FLUTTER', 'Mobile Flutter', 'Khóa học lập trình Mobile App với Flutter', 'ACTIVE'),
                                                          ('BUSINESS_ANALYST', 'Business Analyst', 'Khóa học phân tích nghiệp vụ', 'ACTIVE');

-- Seed Lead Statuses
INSERT INTO lead_statuses (code, name, order_index, is_final) VALUES
                                                                  ('NEW', 'Lead mới', 1, FALSE),
                                                                  ('CONTACTED', 'Đã liên hệ', 2, FALSE),
                                                                  ('CONSULTING', 'Đang tư vấn', 3, FALSE),
                                                                  ('CONSIDERING', 'Đang cân nhắc', 4, FALSE),
                                                                  ('DEPOSITED', 'Đã đặt cọc', 5, FALSE),
                                                                  ('REGISTERED', 'Đã đăng ký', 6, TRUE),
                                                                  ('CALL_BACK', 'Hẹn gọi lại', 7, FALSE),
                                                                  ('NO_ANSWER', 'Không nghe máy', 8, FALSE),
                                                                  ('NOT_SUITABLE', 'Không phù hợp', 9, TRUE),
                                                                  ('CANCELED', 'Hủy đăng ký', 10, TRUE);

-- Seed Lead Sources
INSERT INTO lead_sources (code, name) VALUES
                                          ('FACEBOOK', 'Facebook'),
                                          ('TIKTOK', 'TikTok'),
                                          ('MESSENGER', 'Messenger'),
                                          ('ZALO', 'Zalo'),
                                          ('LANDING_PAGE', 'Landing Page'),
                                          ('GOOGLE_FORM', 'Google Form'),
                                          ('LEAD_MAGNET', 'Tải tài liệu'),
                                          ('REFERRAL', 'Giới thiệu'),
                                          ('ORGANIC', 'Tìm kiếm tự nhiên'),
                                          ('OTHER', 'Khác');
