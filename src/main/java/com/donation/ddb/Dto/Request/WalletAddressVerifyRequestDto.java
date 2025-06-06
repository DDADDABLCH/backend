package com.donation.ddb.Dto.Request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WalletAddressVerifyRequestDto {

    private String walletAddress;

    private String message;

    private String signature;
}
