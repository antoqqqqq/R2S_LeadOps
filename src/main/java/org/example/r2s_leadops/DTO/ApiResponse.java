package org.example.r2s_leadops.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Trạng thái xử lý
     */
    private Boolean success;

    /**
     * Mã lỗi hoặc mã thành công
     */
    private String code;

    /**
     * Thông báo
     */
    private String message;

    /**
     * Dữ liệu trả về
     */
    private T data;

    /**
     * Danh sách lỗi validate (nếu có)
     */
    private List<String> errors;
}
