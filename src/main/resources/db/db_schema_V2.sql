-- ==========================================
-- PostgreSQL Schema & Init Data
-- R2S LeadOps -- v2
--
-- Thay đổi so với v1, theo quyết định thống nhất với API (R2S LeadOps API v1.0.0):
--
-- 1) "Lead" trong API = 1 dòng trong LEAD_OPPORTUNITIES (không phải 1 dòng LEADS).
--    LEADS chỉ còn giữ thông tin định danh dùng chung cho mọi opportunity của
--    cùng một người (fullName, phone, email, manychatId, messengerId, zaloUid,
--    school, major, year_of_study, city).
--    LEAD_OPPORTUNITIES mang personId (= lead_id) để client biết các
--    opportunity nào thuộc cùng một người, và bắt buộc gắn với course_id.
--
-- 2) Toàn bộ enum (role, lead_stage, lead_source, activity_type,
--    activity_result, appointment_status/channel/result, notification_type,
--    notification_reference_type, user_status) đổi theo đúng enum trong
--    OpenAPI spec.
--
-- 3) Mô hình điểm số đổi từ 1 cột `score` sang 4 cột:
--    fit_score / engagement_score / intent_score / total_score,
--    theo đúng LeadResponse của API.
--
-- 4) Field có trong API nhưng thiếu trong DB (manychatId, messengerId,
--    zaloUid, careerGoal, painPoint, startTimeline, preferredChannel,
--    doNotContact, durationMinutes, note, cancelReason, result...)
--    được bổ sung.
-- ==========================================


-- ==========================================
-- 0. ENUM TYPES (theo đúng enum của OpenAPI spec)
-- ==========================================

CREATE TYPE user_role AS ENUM ('ADMIN', 'MANAGER', 'STAFF');

CREATE TYPE user_status AS ENUM ('ACTIVE', 'LOCKED');

CREATE TYPE lead_stage AS ENUM (
    'NEW', 'NURTURE', 'WARM', 'HOT', 'SALE', 'WON', 'LOST'
);

CREATE TYPE lead_source_enum AS ENUM (
    'FACEBOOK', 'INSTAGRAM', 'LANDING_PAGE', 'GOOGLE_FORM',
    'ZALO', 'WEBSITE', 'REFERRAL', 'OTHER'
);

CREATE TYPE activity_type_enum AS ENUM (
    'CALL', 'MESSAGE', 'EMAIL', 'ZALO', 'MEETING',
    'NOTE', 'FOLLOW_UP', 'CONSULTATION'
);

CREATE TYPE activity_result_enum AS ENUM (
    'CONNECTED', 'NO_ANSWER', 'INTERESTED', 'NOT_INTERESTED',
    'CALLBACK', 'SUCCESS', 'FAILED', 'OTHER'
);

CREATE TYPE appointment_status_enum AS ENUM (
    'SCHEDULED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'NO_SHOW'
);

CREATE TYPE appointment_channel_enum AS ENUM (
    'PHONE', 'MESSENGER', 'ZALO', 'EMAIL', 'GOOGLE_MEET', 'OFFLINE', 'OTHER'
);

CREATE TYPE appointment_result_enum AS ENUM (
    'INTERESTED', 'NOT_INTERESTED', 'CALLBACK', 'SUCCESS', 'FAILED', 'OTHER'
);

CREATE TYPE notification_type_enum AS ENUM (
    'APPOINTMENT_CREATED', 'APPOINTMENT_UPDATED', 'APPOINTMENT_CANCELLED',
    'APPOINTMENT_REMINDER', 'APPOINTMENT_COMPLETED',
    'LEAD_ASSIGNED', 'LEAD_UPDATED', 'LEAD_HOT', 'SYSTEM'
);

CREATE TYPE notification_reference_type_enum AS ENUM (
    'APPOINTMENT', 'LEAD', 'SYSTEM'
);


-- ==========================================
-- 1. ROLES
-- ==========================================

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    code user_role NOT NULL UNIQUE,
    description VARCHAR(255)
);


-- ==========================================
-- 2. USERS
-- ==========================================

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT REFERENCES roles(id) ON DELETE RESTRICT,
    status user_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ==========================================
-- 3. COURSES
-- ==========================================

CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);


-- ==========================================
-- 4. LEAD STATUSES
-- ==========================================

CREATE TABLE lead_statuses (
    id SERIAL PRIMARY KEY,
    code lead_stage NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    order_index INT DEFAULT 0,
    is_final BOOLEAN DEFAULT FALSE
);


-- ==========================================
-- 5. LEAD SOURCES
-- ==========================================

CREATE TABLE lead_sources (
    id SERIAL PRIMARY KEY,
    code lead_source_enum NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT
);


-- ==========================================
-- 6. CAMPAIGNS
-- ==========================================

CREATE TABLE campaigns (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE,
    name VARCHAR(100) NOT NULL,
    course_id INT REFERENCES courses(id) ON DELETE SET NULL,
    start_date DATE,
    end_date DATE,
    utm_source VARCHAR(100),
    utm_medium VARCHAR(100),
    utm_campaign VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);


-- ==========================================
-- 7. LEADS (identity / person)
-- ==========================================

CREATE TABLE leads (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    normalized_phone VARCHAR(20),
    email VARCHAR(100),
    manychat_id VARCHAR(100),
    messenger_id VARCHAR(100),
    zalo_uid VARCHAR(100),
    school VARCHAR(150),
    major VARCHAR(100),
    year_of_study VARCHAR(50),
    city VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ==========================================
-- 8. LEAD OPPORTUNITIES (API "Lead")
-- ==========================================

CREATE TABLE lead_opportunities (
    id SERIAL PRIMARY KEY,
    lead_id INT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    course_id INT NOT NULL REFERENCES courses(id) ON DELETE RESTRICT,
    campaign_id INT REFERENCES campaigns(id) ON DELETE SET NULL,
    source_id INT REFERENCES lead_sources(id) ON DELETE SET NULL,
    status_id INT REFERENCES lead_statuses(id) ON DELETE SET NULL,
    assigned_user_id INT REFERENCES users(id) ON DELETE SET NULL,
    current_level VARCHAR(50),
    career_goal TEXT,
    pain_point TEXT,
    start_timeline VARCHAR(100),
    preferred_channel VARCHAR(100),
    fit_score INT NOT NULL DEFAULT 0 CHECK (fit_score BETWEEN 0 AND 100),
    engagement_score INT NOT NULL DEFAULT 0 CHECK (engagement_score BETWEEN 0 AND 100),
    intent_score INT NOT NULL DEFAULT 0 CHECK (intent_score BETWEEN 0 AND 100),
    total_score INT NOT NULL DEFAULT 0 CHECK (total_score BETWEEN 0 AND 100),
    do_not_contact BOOLEAN NOT NULL DEFAULT FALSE,
    last_activity_at TIMESTAMP,
    next_action_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_lead_course UNIQUE (lead_id, course_id)
);


-- ==========================================
-- 9. LEAD ACTIVITIES
-- ==========================================

CREATE TABLE lead_activities (
    id SERIAL PRIMARY KEY,
    opportunity_id INT NOT NULL REFERENCES lead_opportunities(id) ON DELETE CASCADE,
    activity_type activity_type_enum NOT NULL,
    content TEXT NOT NULL,
    result activity_result_enum,
    next_action TEXT,
    next_action_at TIMESTAMP,
    old_status_id INT REFERENCES lead_statuses(id) ON DELETE SET NULL,
    new_status_id INT REFERENCES lead_statuses(id) ON DELETE SET NULL,
    performed_by INT REFERENCES users(id) ON DELETE SET NULL,
    performed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ==========================================
-- 10. LEAD SCORE EVENTS
-- ==========================================

CREATE TABLE lead_score_events (
    id SERIAL PRIMARY KEY,
    opportunity_id INT NOT NULL REFERENCES lead_opportunities(id) ON DELETE CASCADE,
    score_type VARCHAR(20) NOT NULL CHECK (score_type IN ('FIT', 'ENGAGEMENT', 'INTENT', 'TOTAL')),
    rule_code VARCHAR(50),
    description TEXT,
    score_change INT NOT NULL,
    score_after INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ==========================================
-- 11. LEAD ASSIGNMENTS
-- ==========================================

CREATE TABLE lead_assignments (
    id SERIAL PRIMARY KEY,
    opportunity_id INT NOT NULL REFERENCES lead_opportunities(id) ON DELETE CASCADE,
    assigned_from INT REFERENCES users(id) ON DELETE SET NULL,
    assigned_to INT REFERENCES users(id) ON DELETE SET NULL,
    assigned_by INT REFERENCES users(id) ON DELETE SET NULL,
    reason TEXT,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ==========================================
-- 12. APPOINTMENTS
-- ==========================================

CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    opportunity_id INT NOT NULL REFERENCES lead_opportunities(id) ON DELETE CASCADE,
    assigned_user_id INT REFERENCES users(id) ON DELETE SET NULL,
    title VARCHAR(255) NOT NULL,
    appointment_at TIMESTAMP NOT NULL,
    duration_minutes INT NOT NULL DEFAULT 30 CHECK (duration_minutes BETWEEN 5 AND 480),
    channel appointment_channel_enum NOT NULL,
    status appointment_status_enum NOT NULL DEFAULT 'SCHEDULED',
    result appointment_result_enum,
    note TEXT,
    cancel_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ==========================================
-- 13. APPOINTMENT REMINDERS
-- ==========================================

CREATE TABLE appointment_reminders (
    id SERIAL PRIMARY KEY,
    appointment_id INT NOT NULL REFERENCES appointments(id) ON DELETE CASCADE,
    reminder_minutes INT NOT NULL,
    remind_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    sent_at TIMESTAMP
);


-- ==========================================
-- 14. NOTIFICATIONS
-- ==========================================

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    type notification_type_enum NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    reference_type notification_reference_type_enum,
    reference_id INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ==========================================
-- 15. NOTIFICATION RECIPIENTS
-- ==========================================

CREATE TABLE notification_recipients (
    id SERIAL PRIMARY KEY,
    notification_id INT NOT NULL REFERENCES notifications(id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP
);


-- ==========================================
-- 16. WEBHOOK EVENTS
-- ==========================================

CREATE TABLE webhook_events (
    id SERIAL PRIMARY KEY,
    source VARCHAR(50) NOT NULL,
    external_event_id VARCHAR(100),
    payload JSONB,
    processing_status VARCHAR(50),
    error_message TEXT,
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP
);


-- ==========================================
-- 17. AUDIT LOGS
-- ==========================================

CREATE TABLE audit_logs (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50),
    entity_id INT,
    old_value JSONB,
    new_value JSONB,
    ip_address VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ==========================================
-- INDEX
-- ==========================================

CREATE INDEX idx_leads_phone ON leads(normalized_phone);
CREATE INDEX idx_leads_email ON leads(email);
CREATE INDEX idx_opportunities_lead ON lead_opportunities(lead_id);
CREATE INDEX idx_opportunities_course ON lead_opportunities(course_id);
CREATE INDEX idx_opportunities_status ON lead_opportunities(status_id);
CREATE INDEX idx_opportunities_assigned_user ON lead_opportunities(assigned_user_id);
CREATE INDEX idx_activities_opportunity ON lead_activities(opportunity_id);
CREATE INDEX idx_score_events_opportunity ON lead_score_events(opportunity_id);
CREATE INDEX idx_assignments_opportunity ON lead_assignments(opportunity_id);
CREATE INDEX idx_appointments_opportunity ON appointments(opportunity_id);
CREATE INDEX idx_appointments_assigned_user ON appointments(assigned_user_id);
CREATE INDEX idx_appointments_appointment_at ON appointments(appointment_at);
CREATE INDEX idx_notifications_recipient ON notification_recipients(user_id);
CREATE INDEX idx_notifications_read ON notification_recipients(user_id, is_read);
CREATE INDEX idx_webhook_external_event ON webhook_events(source, external_event_id);


-- ==========================================
-- SEED DATA
-- ==========================================

INSERT INTO roles (code, description) VALUES
('ADMIN',   'Quản trị viên hệ thống'),
('MANAGER', 'Quản lý / Trưởng nhóm'),
('STAFF',   'Nhân viên tư vấn (Sales/Marketing)');

INSERT INTO users (
    full_name, email, password_hash, role_id, status
) VALUES (
    'Admin R2S',
    'admin@r2s.edu.vn',
    '$2a$12$Z0J6Q2QyM5Vf1D9B5K.jL.O/8N5Pz8gQ8H4f7A4f/R1f.E.V/M.uO',
    (SELECT id FROM roles WHERE code = 'ADMIN'),
    'ACTIVE'
);

INSERT INTO courses (code, name, description, status) VALUES
('JAVA_BACKEND', 'Java Spring Boot', 'Khóa học lập trình Backend với Java Spring Boot', 'ACTIVE'),
('REACTJS', 'ReactJS', 'Khóa học lập trình Frontend với ReactJS', 'ACTIVE'),
('FLUTTER', 'Mobile Flutter', 'Khóa học lập trình Mobile App với Flutter', 'ACTIVE'),
('BUSINESS_ANALYST', 'Business Analyst', 'Khóa học phân tích nghiệp vụ', 'ACTIVE');

INSERT INTO lead_statuses (code, name, order_index, is_final) VALUES
('NEW',     'Lead mới',           1, FALSE),
('NURTURE', 'Đang nuôi dưỡng',    2, FALSE),
('WARM',    'Tiềm năng',          3, FALSE),
('HOT',     'Rất tiềm năng',      4, FALSE),
('SALE',    'Đang chốt',          5, FALSE),
('WON',     'Đã đăng ký',         6, TRUE),
('LOST',    'Không thành công',   7, TRUE);

INSERT INTO lead_sources (code, name) VALUES
('FACEBOOK',     'Facebook'),
('INSTAGRAM',    'Instagram'),
('LANDING_PAGE', 'Landing Page'),
('GOOGLE_FORM',  'Google Form'),
('ZALO',         'Zalo'),
('WEBSITE',      'Website'),
('REFERRAL',     'Giới thiệu'),
('OTHER',        'Khác');
