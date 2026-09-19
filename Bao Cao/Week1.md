# Đặc tả yêu cầu – R2S LeadOps

## 1. Actors

Hệ thống có 3 nhóm người dùng chính:

| Actor | Chức năng |
| --- | --- |
| **Admin** | Quản lý tài khoản, phân quyền và toàn bộ dữ liệu Lead trong hệ thống |
| **Manager** | Quản lý Lead, theo dõi nhân viên và phân Lead cho Staff |
| **Staff** | Xem và chăm sóc các Lead được phân công, cập nhật trạng thái và lịch sử hoạt động |

Ngoài ra, hệ thống có các **External Systems** như:

- Facebook / Instagram
- ManyChat
- Online Forms
- Make.com

Các hệ thống này cung cấp dữ liệu Lead thông qua API/Webhook.

---

## 2. Features

Các chức năng chính của R2S LeadOps:

### Authentication & Authorization

- Đăng nhập / đăng xuất
- JWT Authentication
- Phân quyền theo `ADMIN`, `MANAGER`, `STAFF`
- Đổi mật khẩu / quên mật khẩu
- Quản lý trạng thái tài khoản

### User Management

- Tạo tài khoản
- Cập nhật thông tin User
- Khóa / mở khóa tài khoản
- Reset mật khẩu
- Quản lý Role

### Lead Management

- Tạo và cập nhật Lead
- Xem danh sách Lead
- Tìm kiếm Lead
- Filter / Sort / Pagination
- Xem chi tiết Lead
- Quản lý Lead Opportunity

### Lead Assignment

- Backend tự động phân Lead cho Staff
- Manager có thể quản lý việc phân Lead
- Theo dõi Staff đang phụ trách Lead

### Lead Scoring

1. `Fit Score`
2. `Engagement Score`
3. `Intent Score`
4. `Total Score`

### Lead Pipeline

1. `NEW`
2. `NURTURE`
3. `WARM`
4. `HOT`
5. `SALE`
6. `WON`
7. `LOST`

### Lead Activity

- Ghi nhận hoạt động chăm sóc
- Theo dõi lịch sử tương tác
- `Last Activity`
- `Next Action`

### Dashboard

- Tổng số Lead
- Thống kê Lead theo Stage
- Thống kê Lead theo nguồn
- Top Leads
- Lead Resource

### Webhook & Integration

- Nhận Lead từ hệ thống bên ngoài
- Xử lý và validate dữ liệu
- Tạo Lead và Lead Opportunity
- Tự động phân Lead
- Tích hợp với Make.com và các nguồn Lead khác

---

## 3. Frontend có gì, Backend có gì?

### Frontend – React Native

Ứng dụng mobile dành cho Admin, Manager và Staff.

Các màn hình chính:

```text
Login
  ↓
Dashboard
  ↓
Lead List
  ├── Search
  ├── Filter
  ├── Sort
  └── Pagination
       ↓
   Lead Detail
       ├── Lead Information
       ├── Opportunity
       ├── Activities
       └── Update Status
```

Ngoài ra:

- User Management
- Lead Assignment
- Profile
- Change Password
- Notifications
- Dashboard / Statistics

### Backend – Java Spring Boot

Backend cung cấp RESTful API và xử lý toàn bộ business logic:

```text
React Native
      ↓
 REST API
      ↓
Controller
      ↓
Service
      ↓
Repository
      ↓
PostgreSQL
```

Các module backend:

- Authentication
- User Management
- Role & Authorization
- Lead Management
- Lead Opportunity
- Lead Assignment
- Lead Scoring
- Lead Activity
- Dashboard
- Webhook
- API Documentation

**Service Layer** chịu trách nhiệm xử lý business logic, ví dụ:

```text
Receive Lead
     ↓
Validate data
     ↓
Create/Find Lead
     ↓
Create Lead Opportunity
     ↓
Calculate Score
     ↓
Assign Staff
     ↓
Save Database
```

---

## 4. Công nghệ sử dụng

| Thành phần | Công nghệ |
| --- | --- |
| **Mobile Frontend** | React Native |
| **Programming Language** | JavaScript / TypeScript |
| **Backend** | Java |
| **Backend Framework** | Spring Boot |
| **Security** | Spring Security |
| **Authentication** | JWT |
| **ORM** | Hibernate |
| **Data Access** | Spring Data JPA |
| **Database** | PostgreSQL |
| **API** | RESTful API |
| **API Documentation** | OpenAPI + Scalar |
| **API Testing** | Postman |
| **Integration / Automation** | Make.com |
| **Webhook** | Spring Boot REST API |
| **Build / Dependency Management** | Maven |
| **Version Control** | Git / GitHub |
| **Development Tools** | IntelliJ IDEA, VS Code |