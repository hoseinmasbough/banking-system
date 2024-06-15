package com.azkivam.banking.exception.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
public class ApiError {

    private LocalDateTime errorDate;

    @JsonProperty("message")
    private String message;


    @Override
    public int hashCode() {
        return Objects.hash(errorDate, message);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ApiError {\n");

        sb.append("    errorDate: ").append(toIndentedString(errorDate)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

