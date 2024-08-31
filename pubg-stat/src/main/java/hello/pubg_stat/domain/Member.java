package hello.pubg_stat.domain;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;
    @Column(name = "account_id")
    private String accountId;
    private String platform;
    @Column(name = "discord_id")
    private String discordId;

    public Member() {
    }

    public Member(String userName, String accountId, String platform) {
        this.userName = userName;
        this.accountId = accountId;
        this.platform = platform;
    }

    public Member(String userName, String accountId, String platform, String discordId) {
        this.userName = userName;
        this.accountId = accountId;
        this.platform = platform;
        this.discordId = discordId;
    }
}


