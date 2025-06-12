package com.donation.ddb.Domain.Exception;

public class BusinessNumberAlreadyExistsException extends RuntimeException {
    public BusinessNumberAlreadyExistsException(String businessNumber) {
        super("이미 등록된 사업자등록번호입니다: " + businessNumber);
    }
}
