package com.twat.detalks.entity;

import com.twat.detalks.vo.Social;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Members")
@DynamicUpdate
public class MemberEntity {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name="member_idx", nullable = false)
    private Long memberIdx;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "member_pwd", nullable = false)
    private String memberPwd;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_isdeleted", nullable = false)
    @Builder.Default
    private Boolean memberIsDeleted = false;

    @Column(name = "member_reason")
    private String memberReason;

    @Column(name ="member_state" ,nullable = false)
    @Builder.Default
    private Boolean memberState = true;

    @Column(name ="member_img" ,nullable = false)
    @Builder.Default
    private String memberImg = "/img/default.png";

    @Column(name="member_summary")
    private String memberSummary;

    @Column(name="member_about")
    private String memberAbout;

    @Column(name="member_rep",nullable = false)
    @Builder.Default
    private int memberRep = 1;

    @Column(name="member_social", nullable = false)
    @Builder.Default
    private Social memberSocial = Social.NONE;

    @Column(name="member_q_count", nullable = false)
    @Builder.Default
    private Integer memberQcount = 0;

    @Column(name="member_a_count", nullable = false)
    @Builder.Default
    private Integer memberAcount = 0;

    @Column(name="member_created", nullable = false)
    @Builder.Default
    private LocalDateTime memberCreated = LocalDateTime.now();

    @Column(name="member_visited", nullable = false)
    @Builder.Default
    private LocalDateTime memberVisited = LocalDateTime.now();;

    @Column(name="member_updated")
    private LocalDateTime memberUpdated;

    @Column(name="member_deleted")
    private LocalDateTime memberDeleted;
}

