Trong ngữ cảnh của lập trình và thiết kế phần
mềm, "**Transfer Object**" là một mô hình thiết kế 
(Design Pattern) được sử dụng để chuyển dữ liệu giữa các 
thành phần trong một hệ thống. Mục tiêu của mô hình này là 
giảm số lượng các cuộc gọi mạng (network calls) và tối 
ưu hóa hiệu suất.

Dưới đây là một số điểm quan trọng về **Transfer Object**:

1. Mục Đích:
+ Chuyển dữ liệu giữa client và server một cách hiệu quả, giảm bớt số lượng cuộc gọi mạng cần thiết.
2. Cấu Trúc:
+ Transfer Object thường là một lớp đơn giản, chứa các trường dữ liệu công khai (public fields) hoặc có các phương thức getter và setter.
3. Hiệu Suất:
+ Được sử dụng để giảm số lượng cuộc gọi mạng và tối ưu hóa hiệu suất bằng cách chuyển gói dữ liệu lớn hơn một lần thay vì nhiều lần nhỏ.
4. Không Chứa Logic:
+ Transfer Object thường không chứa logic kinh doanh hay xử lý dữ liệu phức tạp. Nó chỉ đơn giản là một cấu trúc dữ liệu.
5. Client và Server:
+ Cả client và server đều có một bản sao của Transfer Object. Client sử dụng nó để truy cập dữ liệu từ server và server sử dụng nó để nhận dữ liệu từ client.