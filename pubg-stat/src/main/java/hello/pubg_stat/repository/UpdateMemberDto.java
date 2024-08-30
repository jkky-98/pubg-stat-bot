package hello.pubg_stat.repository;

import lombok.Data;

@Data
public class UpdateMemberDto {

    private Long id;
    private String userName;
    private String accountId;
    private String platform;

    public UpdateMemberDto() {
    }

    public UpdateMemberDto(String userName, String accountId, String platform) {
        this.userName = userName;
        this.accountId = accountId;
        this.platform = platform;
    }
}


